package at.codersbay.courseapp.api.course.create;

public class CreateCourseDTO {
    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
