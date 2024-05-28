package com.suhoi.demo.aspect;

import com.suhoi.demo.out.event.UserActionEvent;
import com.suhoi.demo.out.KafkaProducer;
import com.suhoi.demo.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final UserUtils userUtils;
    private final KafkaProducer kafkaProducer;

    @Pointcut("@annotation(com.suhoi.demo.annotation.Auditable) && execution(* *(..))")
    public void auditMethod() {
    }

    /**
     * Аудит действией пользователя. Действия аутентицифицированного пользователя через этот аспект
     * отправляются в кафку
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("auditMethod()")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String username = null;
        UserActionEvent userActionEvent;
        Object proceed;
        try {
            username = userUtils.getCurrentUser().getUsername();
            proceed = joinPoint.proceed();
        } catch (NullPointerException e) {
            userActionEvent = UserActionEvent.builder()
                    .username(null)
                    .action("Method " + methodName + " was called, but User not authenticated.")
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            kafkaProducer.sendUserAction(userActionEvent);
            proceed = joinPoint.proceed();
            return proceed;
        }
        userActionEvent = UserActionEvent.builder()
                .username(username)
                .action("Method " + methodName + " executed")
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        kafkaProducer.sendUserAction(userActionEvent);
        return proceed;
    }
}
