package at.codersbay.courseapp.api.booking.delete;

import at.codersbay.courseapp.api.booking.Booking;
import at.codersbay.courseapp.api.booking.BookingId;
import at.codersbay.courseapp.api.booking.BookingRepository;
import at.codersbay.courseapp.api.booking.BookingResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/booking/")
public class DeleteBookingController {

    @Autowired
    private BookingRepository bookingRepository;


    /**
     * Rest Path - DELETE-REQUEST: "localhost:8081/api/booking/"
     * Method finds the booking to the unique combination of courseId and userId and deletes the specific booking if it exists.
     *
     * @param deleteBookingDTO - The RequestBody should be a JSON object with the parameters: courseId (Long), userId (Long). i.e.:
     *                         {
     *                         "courseId" : 2,
     *                         "userId": 1
     *                         }
     * @return - ResponseBody incl. confirming response message, StatusCode 200 (OK)
     * - in case the booking can't be found in the database: ResponseBody incl. errorMessage, StatusCode 404 (NOT_FOUND)
     * - in case the booking exists but couldn't be deleted: ResponseBody incl. errorMessage, StatusCode 503 (SERVICE_UNAVAILABLE)
     */
    @DeleteMapping("/")
    public ResponseEntity<BookingResponseBody> deleteBooking(
            @RequestBody
            DeleteBookingDTO deleteBookingDTO) {

        BookingResponseBody bookingResponseBody = new BookingResponseBody();

        BookingId bookingId = new BookingId(deleteBookingDTO.getCourseId(),
                deleteBookingDTO.getUserId());

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            bookingResponseBody.addErrorMessage("The booking with " + bookingId + " doesn't exist.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.NOT_FOUND);
        }

        bookingRepository.deleteById(bookingId);

        optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            bookingResponseBody.addErrorMessage("Could not delete booking with " + bookingId + " .");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.SERVICE_UNAVAILABLE);
        }
        bookingResponseBody.addMessage("Booking with " + bookingId + " deleted.");
        return new ResponseEntity<>(bookingResponseBody, HttpStatus.OK);

    }

}
