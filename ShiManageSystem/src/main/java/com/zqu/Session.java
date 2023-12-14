package com.zqu;

/**
 * @aurhor Dedryck
 * @create 2023-11-28-21:10
 * @description:
 */
public class Session {
    private static String currentAdminUserId;

    public static void setCurrentAdminUserId(String userId) {
        currentAdminUserId = userId;
    }

    public static String getCurrentAdminUserId() {
        return currentAdminUserId;
    }
}
