package com.backend.aop;

import com.backend.entity.user.User;
import com.backend.exception.AccessDeniedException;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.MeetingRepository;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserCheckAspect {
    private final MeetingRepository meetingRepository;

    @Around("@annotation(com.backend.annotation.CheckUserNotNull)")
    public Object checkUserNotNull(ProceedingJoinPoint joinPoint) throws Throwable {
        validateNonNullArguments(joinPoint.getArgs());
        return joinPoint.proceed();
    }

    @Around("@annotation(com.backend.annotation.CheckIsOwner)")
    public Object checkIsOwner(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long meetingId = extractLongArgument(args);
        User user = extractUserArgument(args);

        validateOwner(meetingId, user.getId());

        return joinPoint.proceed();
    }

    private void validateNonNullArguments(Object[] args) {
        if (Arrays.stream(args).anyMatch(Objects::isNull)) {
            throw new NotFoundException(ErrorMessages.NOT_EXIST_USER);
        }
    }

    private Long extractLongArgument(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg instanceof Long)
                .map(arg -> (Long) arg)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private User extractUserArgument(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg instanceof User)
                .map(arg -> (User) arg)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_EXIST_USER));
    }

    private void validateOwner(Long meetingId, Long userId) {
        if (!meetingRepository.isUserOwner(meetingId, userId)) {
            throw new AccessDeniedException(ErrorMessages.ACCESS_DENIED);
        }
    }
}
