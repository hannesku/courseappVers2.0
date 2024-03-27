package at.codersbay.courseapp.api.course.create;

public class NumberOfParticipantsException extends Exception{

    public NumberOfParticipantsException (String errorMessage) {
        super (errorMessage);
    }
}
