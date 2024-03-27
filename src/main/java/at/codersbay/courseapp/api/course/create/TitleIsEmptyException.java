package at.codersbay.courseapp.api.course.create;

public class TitleIsEmptyException extends Exception{

    TitleIsEmptyException (String errorMessage) {
        super(errorMessage);
    }
}
