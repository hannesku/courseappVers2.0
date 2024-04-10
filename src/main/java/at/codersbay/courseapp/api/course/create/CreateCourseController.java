package at.codersbay.courseapp.api.course.create;

import at.codersbay.courseapp.api.ResponseBody;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/course/")
public class CreateCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CreateCourseService courseService;

    @PostMapping
    public ResponseEntity<ResponseBody> createNewCourse (
            @RequestBody
            CreateCourseDTO createCourseDTO) {

        CourseResponseBody courseResponseBody = new CourseResponseBody();

        if (createCourseDTO == null) {
            courseResponseBody.addErrorMessage("The Requestbody is empty");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.BAD_REQUEST);
        }

        Optional<Course> optionalCourse = this.courseRepository.findByTitleIgnoreCase(createCourseDTO.getTitle());
        Optional<Course> optionalLowerCaseCourse = this.courseRepository.findByTitleIgnoreCase(createCourseDTO.getTitle().toLowerCase());
        Optional<Course> optionalCapitalizedCourse = this.courseRepository.findByTitleIgnoreCase(createCourseDTO.getCapitalizedTitle());

        if (optionalCourse.isPresent() || optionalLowerCaseCourse.isPresent() || optionalCapitalizedCourse.isPresent()) {
            courseResponseBody.addErrorMessage("The course with the title '" + createCourseDTO.getTitle() + "' exists already.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.CONFLICT);
        }

        Course newCourse = null;

        try {
            newCourse = this.courseService.createCourse(
                createCourseDTO.getTitle(),
                createCourseDTO.getDescription(),
                createCourseDTO.getMaxParticipants(),
                createCourseDTO.getStartDate(),
                createCourseDTO.getEndDate());

        } catch (TitleIsEmptyException |
                 DescriptionIsEmptyException |
                 NumberOfParticipantsException |
                 IncorrectDateException exception) {
            courseResponseBody.addErrorMessage(exception.getMessage());
            return new ResponseEntity<>(courseResponseBody, HttpStatus.BAD_REQUEST);
        }

        if (!courseRepository.findByTitleIgnoreCase(createCourseDTO.getTitle()).isPresent()) {
            courseResponseBody.addErrorMessage("The new course couldn't be saved.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        courseResponseBody.setCourse(newCourse);
        courseResponseBody.addMessage("The new course was created successfully.");
        // return new ResponseEntity<>(courseResponseBody, HttpStatus.OK);
        return ResponseEntity.ok(courseResponseBody);

    }
}
