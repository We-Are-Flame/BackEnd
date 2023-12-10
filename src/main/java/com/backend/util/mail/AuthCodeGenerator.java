package com.backend.util.mail;

import com.backend.exception.ErrorMessages;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
        StringBuilder builder = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
