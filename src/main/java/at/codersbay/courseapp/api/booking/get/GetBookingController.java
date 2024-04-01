package at.codersbay.courseapp.api.booking.get;

import at.codersbay.courseapp.api.booking.Booking;
import at.codersbay.courseapp.api.booking.BookingRepository;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/booking/")
public class GetBookingController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @GetMapping("/")
    public ResponseEntity<List<Booking>> getAllBookings () {
        List<Booking> allBookings = bookingRepository.findAll();

        // BookingResponseBody bookingResponseBody = new BookingResponseBody();
        // bookingResponseBody.setBooking(allBookings);
        // return new ResponseEntity<>(bookingResponseBody, HttpStatus.OK);

        return ResponseEntity.ok(allBookings);


    }



}
