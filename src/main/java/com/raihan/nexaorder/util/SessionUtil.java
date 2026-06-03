package com.raihan.nexaorder.util;

import jakarta.servlet.http.HttpSession;

public final class SessionUtil {

    private SessionUtil() {
    }

    public static Long requireUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Number number) {
            return number.longValue();
        }

        throw new IllegalArgumentException("User session is invalid");
    }
}
