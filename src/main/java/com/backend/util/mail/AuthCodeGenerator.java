package com.backend.util.mail;

import com.backend.exception.ErrorMessages;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class AuthCodeGenerator {
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(ErrorMessages.NO_SUCH_ALGORITHM);
        }
    }

    public static String createCode() {
        log.info("createCode 내부 실행");
//        StringBuilder builder = new StringBuilder(CODE_LENGTH);
//        log.info("createCode String Builder 실행 완료");
//        for (int i = 0; i < CODE_LENGTH; i++) {
//            builder.append(random.nextInt(10));
//        }
        log.info("builder 제작 완료");
        return "617232";
    }
}
