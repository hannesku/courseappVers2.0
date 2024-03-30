package at.codersbay.courseapp.api.course.get;


import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course/")
public class GetCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/")
    public ResponseEntity<List<Course>> getAll() {
        List<Course> allCourses = courseRepository.findAll();
        return ResponseEntity.ok(allCourses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(
            @PathVariable
            Long id) {
        Optional<Course> optionalCourse = this.courseRepository.findById(id);

        if (!optionalCourse.isPresent()) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // Diff: return ResponseEntity.noContent();
        }

        Course course = optionalCourse.get();

        // return ResponseEntity.ok(course);

        CourseResponseBody courseResponseBody = new CourseResponseBody();
        courseResponseBody.addMessage("The course with id " + id + " does exist.");
        courseResponseBody.setCourse(course);
        return new ResponseEntity(courseResponseBody, HttpStatus.FOUND);

    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Course> getCourseByTitle(
            @PathVariable
            String title) {
        Optional<Course> optionalCourse = this.courseRepository.findByTitle(title);

        if (!optionalCourse.isPresent()) {
            CourseResponseBody courseResponseBody = new CourseResponseBody();
            courseResponseBody.addErrorMessage("The course with the title \"" + title + "\" does not exist.");
            return new ResponseEntity(courseResponseBody, HttpStatus.NOT_FOUND);

            // ?? When to use ResponseEntity<>(...)  vs   ResponseEntity(...)   ??

        }

        Course course = optionalCourse.get();

        CourseResponseBody courseResponseBody = new CourseResponseBody();
        courseResponseBody.setCourse(course);
        courseResponseBody.addMessage("The course with the title \"" + title + "\" does exist.");
        ResponseEntity response = new ResponseEntity<>(courseResponseBody, HttpStatus.FOUND);

        // ?? same as before but this time with <>   ??

        return response;


    }

}
