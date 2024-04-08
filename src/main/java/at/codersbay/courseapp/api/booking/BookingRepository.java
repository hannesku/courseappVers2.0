package at.codersbay.courseapp.api.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, BookingId> {



}
