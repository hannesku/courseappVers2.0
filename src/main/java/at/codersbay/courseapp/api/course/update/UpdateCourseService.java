package at.codersbay.courseapp.api.course.update;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UpdateCourseService {

    private CourseRepository courseRepository;

    public Course updateCourse(String description, int maxParticipants) {

        CourseResponseBody response = new CourseResponseBody();

        Course updatedCourse = new Course();

        if (!StringUtils.isEmpty(description)) {
            updatedCourse.setDescription(description);
        }
        if (maxParticipants > 0 && maxParticipants <= 25 ) {
            updatedCourse.setMaxParticipants(maxParticipants);
        }

        return courseRepository.save(updatedCourse);
    }
}
