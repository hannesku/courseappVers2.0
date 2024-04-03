package at.codersbay.courseapp.api.user.create;

public class UsernameIsEmptyException extends Exception{
    public UsernameIsEmptyException (String message) {
        super(message);
    }

}
