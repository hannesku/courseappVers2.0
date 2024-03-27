package at.codersbay.courseapp.api.course.create;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateCourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse (String title, String description, int maxParticipants)
        throws TitleIsEmptyException, DescriptionIsEmptyException, NumberOfParticipantsException {

        if (StringUtils.isEmpty(title)) {
            throw new TitleIsEmptyException("The Course Title is empty.");
        } else if (StringUtils.isEmpty(description)) {
            throw new DescriptionIsEmptyException("The Description is missing.");
        } else if (maxParticipants <= 0) {
            throw new NumberOfParticipantsException("The Number of Participants are not defined.");
        } else if (maxParticipants > 25) {
            throw new NumberOfParticipantsException("The Number of Participants is too high (max. 25).");
        }

        Course course = new Course(title, description, maxParticipants);
        return courseRepository.save(course);
    }

}
