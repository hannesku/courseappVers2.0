package at.codersbay.courseapp.api.user.exceptions;

public class PasswordIsEmptyException extends Exception{
    public PasswordIsEmptyException (String message) {
        super(message);
    }
}
