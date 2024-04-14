package at.codersbay.courseapp.api.user.exceptions;

public class PasswordInsecureException extends Exception {
    public PasswordInsecureException(String message) {
        super (message);
    }
}
