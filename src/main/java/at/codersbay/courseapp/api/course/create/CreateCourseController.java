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

    @PostMapping
    public ResponseEntity<ResponseBody> createNewCourse (
            @RequestBody
            CreateCourseDTO createCourseDTO) {

        CourseResponseBody courseResponseBody = new CourseResponseBody();

        if (createCourseDTO == null || createCourseDTO.getTitle().isEmpty() || createCourseDTO.getMaxParticipants() <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Course> optionalCourse = this.courseRepository.findByTitle(createCourseDTO.getTitle());

        if (optionalCourse.isPresent()) {
            courseResponseBody.addErrorMessage("The course with the title '" + createCourseDTO.getTitle() + "' exists already.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.CONFLICT);
        }


        Course newCourse = new Course(createCourseDTO.getTitle(), createCourseDTO.getDescription(), createCourseDTO.getMaxParticipants());

        try {
            this.courseRepository.save(newCourse);
        } catch (Throwable t) {
            courseResponseBody.addErrorMessage("The new course couldn't be saved.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        courseResponseBody.setCourse(newCourse);
        courseResponseBody.addMessage("The new course was created successfully.");
        // return new ResponseEntity<>(courseResponseBody, HttpStatus.OK);
        return ResponseEntity.ok(courseResponseBody);
    }
}
