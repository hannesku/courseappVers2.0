package at.codersbay.courseapp.api;

import java.util.ArrayList;

public class ResponseBody {

    private static final int MAX_NR_OF_MESSAGES = 3;

    private ArrayList<String> message = new ArrayList<>(MAX_NR_OF_MESSAGES);
    private ArrayList<String> errorMessage = new ArrayList<>(MAX_NR_OF_MESSAGES);

    public boolean addMessage(String newMessage) {
        return this.message.add(newMessage);
    }

    public boolean addErrorMessage(String newMessage) {
        return this.errorMessage.add(newMessage);
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

    public ArrayList<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ArrayList<String> errorMessage) {
        this.errorMessage = errorMessage;
    }
}
