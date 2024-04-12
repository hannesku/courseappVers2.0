package at.codersbay.courseapp.api.course.create;

public class DescriptionIsEmptyException extends Exception {

    public DescriptionIsEmptyException(String message) {
        super(message);
    }
}
