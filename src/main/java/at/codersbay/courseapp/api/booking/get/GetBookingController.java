package at.codersbay.courseapp.api.booking.get;

import at.codersbay.courseapp.api.booking.*;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> allBookings = bookingRepository.findAll();

        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("/{courseId}/{userId}")
    public ResponseEntity<BookingResponseBody> getBookingByCourseIdUserId(
            @PathVariable
            Long courseId,
            @PathVariable
            Long userId) {

        BookingId bookingId = new BookingId(courseId, userId);
        BookingResponseBody bookingResponseBody = new BookingResponseBody();

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            bookingResponseBody.addErrorMessage("The booking with " + bookingId + " doesn't exist.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.NOT_FOUND);
        }

        Booking booking = optionalBooking.get();

        bookingResponseBody.setBooking(booking);
        return new ResponseEntity<>(bookingResponseBody, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserBookingResponseBody> getBookingsOfUser(
            @PathVariable
            Long id) {

        UserBookingResponseBody userBookingResponseBody = new UserBookingResponseBody();

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            userBookingResponseBody.addErrorMessage("The user with id " + id + " doesn't exist.");
            return new ResponseEntity<>(userBookingResponseBody, HttpStatus.NOT_FOUND);
        }


//        THIS WOULD BE TOO SLOW !!!
//
//        List<Booking> allBookings = bookingRepository.findAll();
//        List<Booking> bookings = new ArrayList<>();
//        for (Booking booking: allBookings) {
//            if ((booking.getUser().getId()) == 2) {
//                bookings.add(booking);
//            }


        User user = optionalUser.get();
        userBookingResponseBody.setUserId(id);
        userBookingResponseBody.setBookings(user.getBookings());

        return new ResponseEntity<>(userBookingResponseBody, HttpStatus.OK);

    }

    @GetMapping("/course/{id}")
    public ResponseEntity<CourseBookingResponseBody> getBookingsByCourse (
            @PathVariable
            Long id) {

        CourseBookingResponseBody courseBookingResponseBody = new CourseBookingResponseBody();

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            courseBookingResponseBody.addErrorMessage("The course with the id " + id + " doesn't exist.");
            return new ResponseEntity<>(courseBookingResponseBody, HttpStatus.NOT_FOUND);
        }

        Course course = optionalCourse.get();
        courseBookingResponseBody.setCourseId(id);
        courseBookingResponseBody.setBookings(course.getBookings());

        return new ResponseEntity<>(courseBookingResponseBody, HttpStatus.OK);


    }

}


