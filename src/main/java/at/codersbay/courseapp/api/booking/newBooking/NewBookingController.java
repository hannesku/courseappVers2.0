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

import java.time.LocalDate;
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


    /**
     * Rest Path - POST-Request: "localhost:8081/api/booking/"
     * Method creates a new booking for a user(student) to a specific course.
     * The unique bookingId is embedded and defined as a combination of courseId and userId.
     * The current date is saved together with the new booking to the database.
     *
     * @param newBookingDTO - The RequestBody should be a JSON object with the parameters: courseId (Long), userId (Long). i.e.:
     *                      {
     *                      "courseId" : 2,
     *                      "userId": 1
     *                      }
     * @return - ResponseBody incl. the newly created booking, a message, StatusCode (200, if successful)
     * Note: A message is added in case the course has started already.
     * possible Exceptions and StatusCodes in testing order (No booking in ResponseBody in that case):
     * - Empty RequestBody: StatusCode 400 (BAD_REQUEST)
     * - Booking exists already: errorMessage, StatusCode 400 (BAD_REQUEST)
     * - Course doesn't exist (wrong CourseId): errorMessage, statusCode 404 (NOT_FOUND)
     * - User doesn't exist (wrong UserId): errorMessage, statusCode 404 (NOT_FOUND)
     * - Course has finished already: errorMessage, StatusCode 409 (CONFLICT)
     * - Course is full (maxParticipants reached): errorMessage, StatusCode 409 (CONFLICT)
     * - Booking could not be saved in database: errorMessage, StatusCode 500 (INTERNAL_SERVER_ERROR)
     */
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

        // CHECK IF COURSE HAS FINISHED / STARTED ALREADY:
        LocalDate today = LocalDate.now();
        LocalDate startDate = optionalCourse.get().getStartDate();
        LocalDate endDate = optionalCourse.get().getEndDate();
        if (endDate.isBefore(today)) {
            bookingResponseBody.addErrorMessage("The course with the id " + newBookingDTO.getCourseId() + " has finished already on " + endDate + ".");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.CONFLICT);
        }
        if (startDate.isBefore(today)) {
            bookingResponseBody.addMessage("NOTE!! The course with the id " + newBookingDTO.getCourseId() + " has started already on " + startDate + "!");
        }

        // CHECK FOR MAX USERS/PARTICIPANTS PER COURSE:
        int maxParticipants = chosenCourse.getMaxParticipants();
        int numberOfBookedUsers = chosenCourse.getBookings().size();

//        System.out.println("chosenCourse: maxParticipants = " + maxParticipants + ", nummberOfBookedUsers = " + numberOfBookedUsers);

        if (numberOfBookedUsers >= maxParticipants) {
            bookingResponseBody.addErrorMessage("The course with id " + newBookingDTO.getCourseId() + " is full.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.CONFLICT;
        }


        Booking newBooking = new Booking(chosenCourse, chosenUser);
        this.bookingRepository.save(newBooking);


        // CHECK IF BOOKING WAS SAVED IN DB:
        optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            bookingResponseBody.addErrorMessage("The booking for " + bookingId + " could not be saved.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(bookingResponseBody, HttpStatus.OK);


    }


}


