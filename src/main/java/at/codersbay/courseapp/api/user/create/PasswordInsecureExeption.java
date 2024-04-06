package at.codersbay.courseapp.api.user.create;

public class PasswordInsecureExeption extends Exception {
    public PasswordInsecureExeption (String message) {
        super (message);
    }
}
