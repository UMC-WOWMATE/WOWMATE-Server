package com.wowmate.server.config.common;

public class StringUtil {

    // null, isEmpty 여부 확인
    public static boolean isNullOrEmpty(String str) {
        if (str == null) {
            return true;
        } else if (str.isBlank()) {
            return true;
        } else {
            return false;
        }
    }
}
