package at.codersbay.courseapp.api.user.create;

public class EmailIsEmptyException extends Exception {
    public EmailIsEmptyException (String message) {
        super(message);
    }
}
