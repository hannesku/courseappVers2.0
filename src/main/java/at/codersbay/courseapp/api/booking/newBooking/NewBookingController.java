package at.codersbay.courseapp.api.booking.newBooking;

import at.codersbay.courseapp.api.ResponseBody;
import at.codersbay.courseapp.api.booking.BookingRepository;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking/")
public class NewBookingController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @PostMapping("/")
    public ResponseEntity<ResponseBody> newBooking (
            @RequestBody
            NewBookingDTO newBookingDTO
    ) {
        


    }






}
