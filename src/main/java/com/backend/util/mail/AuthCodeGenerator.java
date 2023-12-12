package com.backend.util.mail;


import lombok.extern.slf4j.Slf4j;
import java.util.Random;

@Slf4j
public class AuthCodeGenerator {
    private static final int CODE_LENGTH = 6;
    private static final Random random = new Random();

    public static String createCode() {
        StringBuilder builder = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
