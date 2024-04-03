package at.codersbay.courseapp.api.user.create;

public class PasswordIsEmptyException extends Exception{
    public PasswordIsEmptyException (String message) {
        super(message);
    }
}
