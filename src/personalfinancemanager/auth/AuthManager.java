package personalfinancemanager.auth;

import personalfinancemanager.dao.UserDAO;
import personalfinancemanager.models.User;
import personalfinancemanager.util.HashUtil;
import personalfinancemanager.util.ConsoleInput;

public class AuthManager {
    private final UserDAO userDAO = new UserDAO();

    public User login() {
        String username = ConsoleInput.readString("Username: ");
        String password = ConsoleInput.readString("Password: ");
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPasswordHash().equals(HashUtil.hashPassword(password))) {
            System.out.println("[OK] Login successful. Welcome, " + user.getUsername() + "!");
            return user;
        } else {
            System.out.println("[X] Invalid credentials.");
            return null;
        }
    }

    public boolean register() {
        String username = ConsoleInput.readString("Choose username: ");
        if (userDAO.findByUsername(username) != null) {
            System.out.println("[X] Username already exists. Try another.");
            return false;
        }
        String password = ConsoleInput.readString("Choose password: ");
        String hash = HashUtil.hashPassword(password);
        User newUser = new User(0, username, hash);
        boolean success = userDAO.save(newUser);
        if (success) {
            System.out.println("[OK] Registration successful.");
        } else {
            System.out.println("[!] Registration failed. Please try again.");
        }
        return success;
    }
}
