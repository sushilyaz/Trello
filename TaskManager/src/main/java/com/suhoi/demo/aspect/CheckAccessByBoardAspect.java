package com.suhoi.demo.aspect;

import com.suhoi.demo.annotation.CheckAccessByBoard;
import com.suhoi.demo.exception.AccessPermissionDeniedException;
import com.suhoi.demo.exception.DataNotFoundException;
import com.suhoi.demo.model.Board;
import com.suhoi.demo.model.User;
import com.suhoi.demo.repository.BoardRepository;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckAccessByBoardAspect {
    private final BoardRepository boardRepository;
    private final UserUtils userUtils;

    /**
     * Проверка доступа к апи аутентифицированного пользователя
     * 
     * @param joinPoint
     * @param checkAccessByBoard
     */
    @Before("@annotation(checkAccessByBoard)")
    public void checkAccess(JoinPoint joinPoint, CheckAccessByBoard checkAccessByBoard) {
        Long boardId = getaLong(joinPoint);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("Board not found"));
        User currentUser = userUtils.getCurrentUser();

        switch (checkAccessByBoard.value()) {
            case MODERATOR:
                if (!board.getModerators().contains(currentUser)) {
                    throw new AccessPermissionDeniedException("You are not moderator");
                }
                break;
            case MEMBER:
                if (!board.getMembers().contains(currentUser)) {
                    throw new AccessPermissionDeniedException("You are not member of board");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown access type");
        }
    }

    private static Long getaLong(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();

        Long boardId = null;

        for (int i = 0; i < args.length; i++) {
            if ("boardId".equals(parameterNames[i]) && args[i] instanceof Long) {
                boardId = (Long) args[i];
                break;
            }
        }

        if (boardId == null) {
            throw new IllegalArgumentException("Board ID not found in method arguments");
        }
        return boardId;
    }
}
