package personalfinancemanager.auth;

import personalfinancemanager.models.User;

public class Session {
    private static User currentUser;

    public static void setUser(User user) {
        currentUser = user;
        System.out.println("[OK] Session started for user: " + user.getUsername());
    }

    public static User getUser() {
        return currentUser;
    }

    public static boolean isAuthenticated() {
        return currentUser != null;
    }

    public static void logout() {
        if (currentUser != null) {
            System.out.println("[Logout] " + currentUser.getUsername() + " has logged out.");
        }
        currentUser = null;
    }
}
