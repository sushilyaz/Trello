package com.suhoi.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LoggableAspect {

    @Pointcut("@annotation(com.suhoi.demo.annotation.Loggable) && execution(* *(..))")
    public void loggableMethod() {
    }

    @Around("loggableMethod()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Calling method {}", methodSignature.toShortString());
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        stopWatch.stop();
        log.info("Execution of method {} finished. Execution time is {} ms", methodSignature.toShortString(), endTime - startTime);
        return result;
    }
}
