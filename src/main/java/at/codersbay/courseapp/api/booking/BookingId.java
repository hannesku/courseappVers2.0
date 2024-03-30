package at.codersbay.courseapp.api.booking;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BookingId implements Serializable {

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "user_id")
    private Long userId;

    protected BookingId () {}

    public BookingId(Long courseId, Long userId) {
        this.courseId = courseId;
        this.userId = userId;
    }



}
