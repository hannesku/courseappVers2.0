package at.codersbay.courseapp.api.course.get;

import java.time.LocalDate;

public class GetCourseDTO {
    private LocalDate searchDate;

    public LocalDate getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(LocalDate searchDate) {
        this.searchDate = searchDate;
    }
}
