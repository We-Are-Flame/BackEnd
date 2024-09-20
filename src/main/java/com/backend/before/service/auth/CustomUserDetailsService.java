package com.backend.before.service.auth;

import com.backend.before.entity.user.User;
import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.JwtAuthenticationException;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import com.backend.before.repository.user.UserRepository;
import com.backend.before.util.wrapper.UserWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private static final String USER_INFO_SEPARATOR = ":";
    private static final String MEETINGS_URL_PATTERN = "^/api/meetings/(\\d+)(/|$)";
    private static final int MEETING_ID_INDEX = 1;

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    @Override
    public UserDetails loadUserByUsername(String userInfo) throws UsernameNotFoundException {
        String[] idAndRoleArray = splitUserInfo(userInfo);
        String userId = idAndRoleArray[0];
        Optional<Long> meetingId = extractMeetingIdFromRequest();

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new JwtAuthenticationException(ErrorMessages.NO_MATCH_TOKEN_AND_USER));

        return meetingId
                .map(id -> createUserWrapper(user, id))
                .orElseGet(() -> createUserWrapperWithoutMeetingId(user));
    }

    private String[] splitUserInfo(String userInfo) {
        String[] idAndRoleArray = userInfo.split(USER_INFO_SEPARATOR);
        validateUserInfo(idAndRoleArray);
        return idAndRoleArray;
    }

    private UserDetails createUserWrapper(User user, Long meetingId) {
        boolean isOwner = meetingRepository.isOwner(meetingId, user.getId());
        return UserWrapper.builder()
                .user(user)
                .meetingId(meetingId)
                .authority(Authority.determineAuthority(isOwner))
                .build();
    }


    private UserDetails createUserWrapperWithoutMeetingId(User user) {
        return UserWrapper.builder()
                .user(user)
                .authority(Authority.ROLE_USER)
                .build();
    }

    private Optional<Long> extractMeetingIdFromRequest() {
        HttpServletRequest request = getCurrentHttpRequest();
        String path = request.getRequestURI();
        return parseMeetingIdFromPath(path);
    }

    private HttpServletRequest getCurrentHttpRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    private Optional<Long> parseMeetingIdFromPath(String path) {
        Matcher matcher = Pattern.compile(MEETINGS_URL_PATTERN).matcher(path);
        if (matcher.find()) {
            try {
                return Optional.of(Long.parseLong(matcher.group(MEETING_ID_INDEX)));
            } catch (NumberFormatException e) {
                log.error("Invalid meeting ID in URL", e);
            }
        }
        return Optional.empty();
    }

    private void validateUserInfo(String[] idAndRoleArray) {
        if (idAndRoleArray.length != 2 || idAndRoleArray[0].isEmpty() || idAndRoleArray[1].isEmpty()) {
            throw new JwtAuthenticationException(ErrorMessages.INVALID_PAYLOAD);
        }
    }
}
