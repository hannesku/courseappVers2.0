package at.codersbay.courseapp.api.user.exceptions;

public class EmailIsEmptyException extends Exception {
    public EmailIsEmptyException (String message) {
        super(message);
    }
}
