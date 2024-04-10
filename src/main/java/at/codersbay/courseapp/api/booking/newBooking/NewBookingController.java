package at.codersbay.courseapp.api.booking.newBooking;

import at.codersbay.courseapp.api.ResponseBody;
import at.codersbay.courseapp.api.booking.Booking;
import at.codersbay.courseapp.api.booking.BookingId;
import at.codersbay.courseapp.api.booking.BookingRepository;
import at.codersbay.courseapp.api.booking.BookingResponseBody;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public ResponseEntity<ResponseBody> newBooking(
            @RequestBody
            NewBookingDTO newBookingDTO) {

        BookingResponseBody bookingResponseBody = new BookingResponseBody();


        // CHECK IF REQUESTBODY IS EMPTY:
        if (newBookingDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BookingId bookingId = new BookingId(newBookingDTO.getCourseId(), newBookingDTO.getUserId());


        // CHECK IF BOOKING EXISTS ALREADY:
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            bookingResponseBody.addErrorMessage("The booking with " + bookingId + " exists already.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.BAD_REQUEST);
        }


        // CHECK IF COURSE EXISTS:
        Optional<Course> optionalCourse = courseRepository.findById(newBookingDTO.getCourseId());
        if (optionalCourse.isEmpty()) {
            bookingResponseBody.addErrorMessage("The course with the id " + newBookingDTO.getCourseId() + " doesn't exist.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.NOT_FOUND);
        }
        Course chosenCourse = optionalCourse.get();


        // CHECK IF USER EXISTS:
        Optional<User> optionalUser = userRepository.findById(newBookingDTO.getUserId());
        if (optionalUser.isEmpty()) {
            bookingResponseBody.addErrorMessage("The user with the id " + newBookingDTO.getUserId() + " doesn't exist.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.NOT_FOUND);
        }
        User chosenUser = optionalUser.get();


        // CHECK FOR MAX USERS/PARTICIPANTS PER COURSE:
        int maxParticipants = chosenCourse.getMaxParticipants();
        int numberOfBookedUsers = chosenCourse.getBookings().size();

//        System.out.println("chosenCourse: maxParticipants = " + maxParticipants + ", nummberOfBookedUsers = " + numberOfBookedUsers);

        if (numberOfBookedUsers >= maxParticipants) {
            bookingResponseBody.addErrorMessage("The course with id " + newBookingDTO.getCourseId() + " is full.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.BAD_REQUEST);
        }


        Booking newBooking = new Booking(chosenCourse, chosenUser);
        this.bookingRepository.save(newBooking);


        // CHECK IF BOOKING WAS SAVED IN DB:
        optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            bookingResponseBody.addErrorMessage("The booking for " + bookingId + " could not be saved.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);


    }


}


