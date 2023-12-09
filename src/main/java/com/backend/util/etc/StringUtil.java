package com.backend.util.etc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtil {
    private static final String DELIMITER = ",";

    private StringUtil() {
    }

    public static List<String> split(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split(DELIMITER));
    }
}
