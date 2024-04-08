package at.codersbay.courseapp.api.booking;

import at.codersbay.courseapp.api.ResponseBody;

import java.util.List;

public class UserBookingResponseBody extends ResponseBody {

    private Long userId;

    private List<Booking> bookings;

    public UserBookingResponseBody() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
