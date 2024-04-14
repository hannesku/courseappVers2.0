package at.codersbay.courseapp.api.user.exceptions;

public class EmailAlreadyExistsException extends Exception {
    public EmailAlreadyExistsException(String message) {
        super (message);
    }
}
