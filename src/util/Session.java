package util;

public class Session {

    public static int userId;
    public static String userName;
    public static String email;

    public static void clear() {
        userId = 0;
        userName = null;
        email = null;
    }
}