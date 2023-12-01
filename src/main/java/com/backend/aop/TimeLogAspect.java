package com.backend.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimeLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeLogAspect.class);

    @Around("execution(* com.backend.service..*(..))")
    public Object AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed(); // 조인포인트의 메서드 실행 후 결과를 반환

        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        LOGGER.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);

        return result; // 실행 결과 반환
    }
}

