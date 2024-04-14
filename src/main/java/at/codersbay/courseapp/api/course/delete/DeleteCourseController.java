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


    /**
     * Rest Path for DELETE-Request: "localhost:8081/api/course/{id}"
     * Method finds the course of a specific id in the database, checks if it exists and if so removes it from the database.
     * NOTE: If bookings for the wanted course exist, they will also be removed from the bookings-List.
     *
     * @param id - Id (int) of the wanted course
     * @return - ResponseBody incl. confirmation message, StatusCode 200 (OK)
     * - ResponseBody incl errorMessage if the course doesn't exist in the database, StatusCode 404 (NOT_FOUND)
     * - ResponseBody incl errorMessage if the course exists but couldn't be deleted, StatusCode 503 (SERVICE_UNAVAILABLE)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteCourseById(
            @PathVariable
            Long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        CourseResponseBody courseResponseBody = new CourseResponseBody();


        if (!optionalCourse.isPresent()) {
            courseResponseBody.addErrorMessage("The course with the Id " + id + " does not exist.");
            return new ResponseEntity(courseResponseBody, HttpStatus.NOT_FOUND);
        }

        courseRepository.deleteById(id);

        optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            courseResponseBody.addErrorMessage("The course with the id " + id + " could not be deleted.");
            return new ResponseEntity(courseResponseBody, HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            courseResponseBody.addMessage("The course with the id " + id + " was deleted.");
            return new ResponseEntity(courseResponseBody, HttpStatus.OK);
        }

    }


    /**
     * Rest Path for DELETE-Request: "localhost:8081/api/course/{title}"
     * Method finds the course of a specific title in the database, checks if it exists and if yes removes it from the database.
     *
     * @param title - Title (String) of the wanted course
     * @return - ResponseBody incl. pos. response message, StatusCode 200 (OK)
     * - ResponseBody incl errorMessage if the course doesn't exist in the database, StatusCode 404 (NOT_FOUND)
     * - ResponseBody incl errorMessage if the course was found but couldn't be deleted, StatusCode 503 (SERVICE_UNAVAILABLE)
     */
    @DeleteMapping("title/{title}")
    public ResponseEntity<ResponseBody> deleteCourseByTitle(
            @PathVariable
            String title) {

        CourseResponseBody courseResponseBody = new CourseResponseBody();
        Optional<Course> optionalCourse = courseRepository.findByTitleIgnoreCase(title);

        if (!optionalCourse.isPresent()) {
            courseResponseBody.addErrorMessage("The course with the title '" + title + "' does not exist.");
            return new ResponseEntity(courseResponseBody, HttpStatus.NOT_FOUND);
        }

        Course courseToDelete = optionalCourse.get();
        courseRepository.deleteById(courseToDelete.getId());

        optionalCourse = courseRepository.findByTitleIgnoreCase(title);

        if (optionalCourse.isPresent()) {
            courseResponseBody.addErrorMessage("The course with the title '" + title + "' could not be deleted:");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            courseResponseBody.addMessage("The course with the title '" + title + "' was deleted.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.OK);
        }
    }

}
