package at.codersbay.courseapp.api.user.create;

public class UserAlreadyExistsExeption extends Exception {
    public UserAlreadyExistsExeption (String message) {
        super(message);
    }
}
