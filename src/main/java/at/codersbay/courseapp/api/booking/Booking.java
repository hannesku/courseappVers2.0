package at.codersbay.courseapp.api.booking;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_BOOKING")
public class Booking {
    @EmbeddedId
    private BookingId id;

    @JsonIgnore
    @ManyToOne
    @MapsId("courseId")
    private Course course;

    @JsonIgnore
    @OneToOne
    @MapsId("userId")
    private User user;

    @Column
    private LocalDate bookingDate;

    public Booking() {
    }

    public Booking(Course course, User user) {
        this.course = course;
        this.user = user;
    }

    public BookingId getId() {
        return id;
    }

    public void setId(BookingId id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
}


