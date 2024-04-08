package at.codersbay.courseapp.api.booking.newBooking;

import at.codersbay.courseapp.api.ResponseBody;
import at.codersbay.courseapp.api.booking.Booking;
import at.codersbay.courseapp.api.booking.BookingId;
import at.codersbay.courseapp.api.booking.BookingRepository;
import at.codersbay.courseapp.api.booking.BookingResponseBody;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
            NewBookingDTO newBookingDTO) {

        BookingResponseBody bookingResponseBody = new BookingResponseBody();

        if (newBookingDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BookingId bookingId = new BookingId(newBookingDTO.getCourseId(), newBookingDTO.getUserId());

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (optionalBooking.isPresent()) {
            bookingResponseBody.addErrorMessage("The booking with " + bookingId + " exists already.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.BAD_REQUEST);
        }



            Booking newBooking = optionalBooking.get();
//            newBooking.setUser(userRepository.findById(newBookingDTO.getUserId()).get());
//            newBooking.setCourse(courseRepository.findById(newBookingDTO.getCourseId()).get());
            this.bookingRepository.save(newBooking);

        optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            bookingResponseBody.addErrorMessage("The booking for " + bookingId + " could not be saved.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

            return new ResponseEntity<>(HttpStatus.OK);





    }








}


