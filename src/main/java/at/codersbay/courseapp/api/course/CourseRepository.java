package at.codersbay.courseapp.api.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {

    public Optional<Course> findByTitleIgnoreCase(String title);

    @Query("SELECT c FROM Course c " +
            "WHERE :searchDate BETWEEN c.startDate AND c.endDate")
    Set<Course> findOngoingCoursesAtDate(@Param("searchDate") LocalDate searchDate);


    @Query("SELECT c FROM Course c " +
            "WHERE :searchDate <= c.startDate")
    Set<Course> findCoursesAfterDate(@Param("searchDate") LocalDate searchDate);

    @Query("SELECT c from Course c where c.startDate > :searchDate and c.bookings.size < c.maxParticipants")
    Set<Course> findAvailableCoursesAfterDate (@Param("searchDate") LocalDate searchDate);

}
