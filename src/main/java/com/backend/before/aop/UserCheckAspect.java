package com.backend.before.aop;

import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.NotFoundException;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class UserCheckAspect {
    @Around("@annotation(com.backend.before.annotation.CheckUserNotNull)")
    public Object checkUserNotNull(ProceedingJoinPoint joinPoint) throws Throwable {
        validateNonNullArguments(joinPoint.getArgs());
        return joinPoint.proceed();
    }

    private void validateNonNullArguments(Object[] args) {
        if (Arrays.stream(args).anyMatch(Objects::isNull)) {
            throw new NotFoundException(ErrorMessages.NOT_EXIST_USER);
        }
    }
}
