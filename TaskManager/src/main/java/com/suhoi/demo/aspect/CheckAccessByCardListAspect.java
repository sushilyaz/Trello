package com.suhoi.demo.aspect;

import com.suhoi.demo.annotation.CheckAccessByBoard;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.model.CardList;
import com.suhoi.demo.repository.CardListRepository;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckAccessByCardListAspect {
    private final CardListRepository cardListRepository;
    private final UserUtils userUtils;

    /**
     * Проверка доступа к апи
     *
     * @param joinPoint
     * @param checkAccessByBoard
     * @return
     * @throws Throwable
     */
    @Around("@annotation(checkAccessByBoard)")
    public Object checkAccess(ProceedingJoinPoint joinPoint, CheckAccessByBoard checkAccessByBoard) throws Throwable {
        Long cardListId = getaLong(joinPoint);

        if (cardListId == null) {
            System.out.println("No card list parameter");
            return joinPoint.proceed();
        }

        CardList cardList = cardListRepository.findById(cardListId)
                .orElseThrow(() -> new DataNotFoundException("CardList not found"));

        if (!cardList.getBoard().getMembers().contains(userUtils.getCurrentUser())) {
            throw new AccessPermissionDeniedException("You do not have permission to access this endpoint");
        }

        return joinPoint.proceed();
    }

    private static Long getaLong(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();

        Long cardListId = null;

        for (int i = 0; i < args.length; i++) {
            if ("cardListId".equals(parameterNames[i]) && args[i] instanceof Long) {
                cardListId = (Long) args[i];
                break;
            }
        }

        return cardListId;
    }
}
