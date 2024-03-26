package at.codersbay.courseapp.api.course.delete;


import at.codersbay.courseapp.api.ResponseBody;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/course/")
public class DeleteCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteCourseById (
            @PathVariable
            Long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (!optionalCourse.isPresent()) {
            CourseResponseBody courseResponseBody = new CourseResponseBody();
            courseResponseBody.addErrorMessage("The course with the Id " + id + " does not exist.");
            return new ResponseEntity(courseResponseBody, HttpStatus.NOT_FOUND);
        }

        courseRepository.deleteById(id);

        optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            CourseResponseBody courseResponseBody = new CourseResponseBody();
            courseResponseBody.addErrorMessage("The course with the id " + id + " could not be deleted.");
            return new ResponseEntity(courseResponseBody, HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            CourseResponseBody courseResponseBody = new CourseResponseBody();
            courseResponseBody.addMessage("The course with the id " + id + " was deleted.");
            return new ResponseEntity(courseResponseBody, HttpStatus.OK);
        }

    }

}
