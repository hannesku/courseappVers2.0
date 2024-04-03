package at.codersbay.courseapp.api.course;


import at.codersbay.courseapp.api.booking.Booking;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TB_COURSE")
public class Course {
    @Id
    @GeneratedValue(generator = "course-sequence-generator")
    @GenericGenerator(
            name = "course-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "course_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
                    }
    )
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private int maxParticipants;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Course() {

    }

    public Course (String title, String description, int maxParticipants) {
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
