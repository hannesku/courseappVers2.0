package at.codersbay.courseapp.api.user.exceptions;

public class EmailIsNoEmailException extends Exception{
    public EmailIsNoEmailException (String message) {
        super(message);
    }
}
