package at.codersbay.courseapp.api.user.exceptions;

public class UsernameIsEmptyException extends Exception{
    public UsernameIsEmptyException (String message) {
        super(message);
    }

}
