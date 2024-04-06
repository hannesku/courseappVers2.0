package at.codersbay.courseapp.api.user.exceptions;

public class UserAlreadyExistsExeption extends Exception {
    public UserAlreadyExistsExeption (String message) {
        super(message);
    }
}
