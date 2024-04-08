package at.codersbay.courseapp.api.booking;

import at.codersbay.courseapp.api.ResponseBody;

import java.util.List;

public class CourseBookingResponseBody extends ResponseBody {

    private Long courseId;

    private List<Booking> bookings
            ;

    public CourseBookingResponseBody() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
