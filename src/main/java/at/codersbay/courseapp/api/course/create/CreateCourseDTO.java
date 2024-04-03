package at.codersbay.courseapp.api.course.create;

public class CreateCourseDTO {
     private String title;
    private String description;
    private int maxParticipants;


    public String getCapitalizedTitle() {
        String capitalizedTitle = "";
        capitalizedTitle = title.substring(0, 1).toUpperCase() + title.substring(1);
        return capitalizedTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
