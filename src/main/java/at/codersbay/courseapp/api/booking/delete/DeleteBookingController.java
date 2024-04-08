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

    @DeleteMapping("/")
    public ResponseEntity<BookingResponseBody> deleteBooking (
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
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.BAD_REQUEST);
        }
            bookingResponseBody.addMessage("Booking with " + bookingId + " deleted.");
            return new ResponseEntity<>(bookingResponseBody, HttpStatus.OK);

    }

}
