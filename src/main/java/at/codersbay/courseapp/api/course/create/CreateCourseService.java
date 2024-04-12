package at.codersbay.courseapp.api.course.create;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CreateCourseService {

    public static final int generalMaxCourseParticipants = 50;

    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse (String title, String description, int maxParticipants, LocalDate startDate, LocalDate endDate)
        throws TitleIsEmptyException, DescriptionIsEmptyException, NumberOfParticipantsException, IncorrectDateException, DateIsEmptyException {

        if (StringUtils.isEmpty(title)) {
            throw new TitleIsEmptyException("The Course Title is empty.");
        } else if (StringUtils.isEmpty(description)) {
            throw new DescriptionIsEmptyException("The Description is missing.");
        } else if (maxParticipants <= 0) {
            throw new NumberOfParticipantsException("The Number of Participants are not defined.");
        } else if (maxParticipants > generalMaxCourseParticipants) {
            throw new NumberOfParticipantsException("The Number of Participants is too high (max. " + generalMaxCourseParticipants + ").");
        } else if (ObjectUtils.isEmpty(startDate)) {
            throw new DateIsEmptyException("The startDate is empty in the RequestBody.");
        } else if (ObjectUtils.isEmpty(endDate)) {
            throw new DateIsEmptyException("The endDate is empty in the RequestBody.");
        } else if (endDate.isBefore(startDate)) {
            throw new IncorrectDateException("The endDate of the course is before the startDate.");
        }

        Course course = new Course(title, description, maxParticipants, startDate, endDate);
        return courseRepository.save(course);
    }

}
