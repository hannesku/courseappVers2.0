package at.codersbay.courseapp.api.course.create;

import java.time.LocalDate;

public class CreateCourseDTO {
    private String title;
    private String description;
    private int maxParticipants;

    private LocalDate startDate;

    private LocalDate endDate;


    public String getCapitalizedTitle() {
        String capitalizedTitle = "";
        capitalizedTitle = title.substring(0, 1).toUpperCase() + title.substring(1);
        return capitalizedTitle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
