package at.codersbay.courseapp.api.booking.newBooking;

public class NewBookingDTO {

    private Long courseId;

    private Long userId;


    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
