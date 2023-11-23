package com.backend.service.custom;

import com.backend.exception.ErrorMessages;
import com.backend.exception.JwtAuthenticationException;
import com.backend.repository.user.UserRepository;
import com.backend.util.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private static void validateUserInfo(String[] idAndRoleArray) {
        if (idAndRoleArray.length != 2 || idAndRoleArray[0].isEmpty() || idAndRoleArray[1].isEmpty()) {
            throw new JwtAuthenticationException(ErrorMessages.INVALID_PAYLOAD);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userInfo) throws UsernameNotFoundException {
        String[] idAndRoleArray = userInfo.split(":");
        validateUserInfo(idAndRoleArray);

        String id = idAndRoleArray[0];

        return loadMemberById(id);
    }

    private UserDetails loadMemberById(String id) {
        return userRepository.findById(Long.valueOf(id))
                .map(UserWrapper::new)
                .orElseThrow(() -> new JwtAuthenticationException(ErrorMessages.NO_MATCH_TOKEN_AND_USER));
    }
}
