package at.codersbay.courseapp.api.user.create;

public class EmailAlreadyExistsExeption extends Exception {
    public EmailAlreadyExistsExeption (String message) {
        super (message);
    }
}
