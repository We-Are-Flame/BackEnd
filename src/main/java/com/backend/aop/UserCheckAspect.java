package com.backend.aop;

import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserCheckAspect {

    @Around("@annotation(com.backend.annotation.CheckUserNotNull)")
    public Object checkUserNotNull(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null) {
                throw new NotFoundException(ErrorMessages.NOT_EXIST_USER);
            }
        }
        return joinPoint.proceed();
    }
}
