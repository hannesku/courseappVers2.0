package at.codersbay.courseapp.api.course.get;


import at.codersbay.courseapp.api.booking.BookingRepository;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/api/course/")
public class GetCourseController {

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Rest Path: "localhost/8081/api/course/"
     * Method finds all courses in database.
     * @return Responsentity with StatusCode 200. Response includes List<Course>
     */
    @GetMapping("/")
    public ResponseEntity<List<Course>> getAll() {
        List<Course> allCourses = courseRepository.findAll();
        return ResponseEntity.ok(allCourses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseBody> getCourseById(
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
        return new ResponseEntity<>(courseResponseBody, HttpStatus.FOUND);

    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Course> getCourseByTitle(
            @PathVariable
            String title) {
        Optional<Course> optionalCourse = this.courseRepository.findByTitleIgnoreCase(title);

        if (!optionalCourse.isPresent()) {
            CourseResponseBody courseResponseBody = new CourseResponseBody();
            courseResponseBody.addErrorMessage("The course with the title '" + title + "' does not exist.");
            return new ResponseEntity(courseResponseBody, HttpStatus.NOT_FOUND);

            // ?? When to use ResponseEntity<>(...)  vs   ResponseEntity(...)   ??

        }

        Course course = optionalCourse.get();

        CourseResponseBody courseResponseBody = new CourseResponseBody();
        courseResponseBody.setCourse(course);
        courseResponseBody.addMessage("The course with the title '" + title + "' does exist.");
        ResponseEntity response = new ResponseEntity<>(courseResponseBody, HttpStatus.FOUND);

        // ?? same as before but this time with <>   ??

        return response;

    }

    @GetMapping("/ongoing")
    public ResponseEntity<Set<Course>> getAllOngoingCoursesAtDate(
            @RequestBody
            GetCourseDTO getCourseDTO) {

        List<Course> allCourses = courseRepository.findAll();
        Set<Course> ongoingCourses = new HashSet<>();

        for (Course course : allCourses) {
            if (getCourseDTO.getSearchDate().isAfter(course.getStartDate())
                    && getCourseDTO.getSearchDate().isBefore(course.getEndDate())) {
                ongoingCourses.add(course);
            }
        }

        return ResponseEntity.ok(ongoingCourses);

    }


    @GetMapping("/available")
    public ResponseEntity<Set<Course>> getAvailableCoursesAfterDate(
            @RequestBody
            GetCourseDTO getCourseDTO
    ) {

        List<Course> allCourses = courseRepository.findAll();
        Set<Course> availableCourses = new HashSet<>();

        int maxParticipants;
        int numberOfBookings;
        LocalDate startDate;
        LocalDate searchDate = getCourseDTO.getSearchDate();

        for (Course course : allCourses) {
            maxParticipants = course.getMaxParticipants();
            numberOfBookings = course.getBookings().size();
            startDate = course.getStartDate();

            if (numberOfBookings < maxParticipants && startDate.isAfter(searchDate)) {
                availableCourses.add(course);
            }
        }

        return ResponseEntity.ok(availableCourses);

    }

}
