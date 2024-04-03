package at.codersbay.courseapp.api.user.create;

public class EmailIsNoEmailException extends Exception{
    public EmailIsNoEmailException (String message) {
        super(message);
    }
}
