package at.codersbay.courseapp.api.course.update;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateCourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course updateCourse(Course updatedCourse, String description, int maxParticipants) {

        CourseResponseBody response = new CourseResponseBody();

        if (!StringUtils.isEmpty(description)) {
            updatedCourse.setDescription(description);
        }
        if (maxParticipants > 0) {
            updatedCourse.setMaxParticipants(maxParticipants);
        }

        // Exception: more bookings than maxParticipants.


        return courseRepository.save(updatedCourse);
    }
}
