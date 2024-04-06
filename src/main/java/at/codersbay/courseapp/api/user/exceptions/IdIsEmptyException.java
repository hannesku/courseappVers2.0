package at.codersbay.courseapp.api.user.exceptions;

public class IdIsEmptyException extends RuntimeException {
    public IdIsEmptyException (String message) {
        super (message);
    }
}
