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


    /**
     * Rest Path - GET-Request: "localhost:8081/api/booking/"
     * Method finds all bookings in the database and returns them as a List of JSON-Objects.
     *
     * @return - ResponseEntity with StatusCode 200 (OK). The Response includes a List<Booking>
     */
    @GetMapping("/")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> allBookings = bookingRepository.findAll();

        return ResponseEntity.ok(allBookings);
    }


    /**
     * Rest Path - GET-Request: "localhost:8081/api/booking/{courseId}/{userId}"
     * Method finds the booking of a specific bookingId in the database.
     * The unique bookingId is embedded and defined as a combination of courseId and userId.
     * Those 2 Ids have to be provided as pathVariables in the correct order (courseId / userId).
     *
     * @param courseId - Id (Long) of the booked course
     * @param userId - Id (long) of a specific User (Student) of the course.
     * @return - ResponseBody incl. the wanted booking and StatusCode 200 (OK).
     * - In case the wanted booking doesn't exist in the database: errorMessage and StatusCode 302 (NOT_FOUND)
     */
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


    /**
     * Rest Path - GET-Request: "localhost:8081/api/booking/user/{id}"
     * Method finds all bookings of a specific user in the database.
     *
     * @param id - userId (Long) of wanted user.
     * @return - ResponseBody incl. a List of all bookings of the wanted user and StatusCode 200 (OK).
     * - In case the wanted user doesn't exist in the database: errorMessage and StatusCode 302 (NOT_FOUND).
     */
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


    /**
     * Rest Path - GET-Request: "localhost:8081/api/booking/course/{id}"
     * Method finds all bookings for a specific course in the dagebase.
     *
     *
     * @param id - courseId (Long) of wanted course
     * @return - ResponseBody incl. a List of all bookings for the specific course and StatusCode 200 (OK).
     * - In case the wanted course doesn't exist in the database: errorMessage and StatusCode 302 (NOT_FOUND).
     */
    @GetMapping("/course/{id}")
    public ResponseEntity<CourseBookingResponseBody> getBookingsForCourse (
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


