package at.codersbay.courseapp.api.user.exceptions;

public class PasswordInsecureExeption extends Exception {
    public PasswordInsecureExeption (String message) {
        super (message);
    }
}
