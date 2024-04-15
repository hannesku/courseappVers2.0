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
     * Rest Path for GET-Request: "localhost:8081/api/course/"
     * Method finds all courses in the database.
     *
     * @return - ResponseEntity with StatusCode 200 (OK). Response includes List<Course>
     */
    @GetMapping("/")
    public ResponseEntity<List<Course>> getAll() {
        List<Course> allCourses = courseRepository.findAll();
        return ResponseEntity.ok(allCourses);
    }


    /**
     * Rest Path for GET-Request: "localhost:8081/api/course/{id}"
     * Method finds the course of a specific id in the database and checks if the wanted course actually exists.
     *
     * @param id - Id (Long) of the wanted course
     * @return - ResponseBody incl. the wanted course, a pos. response message and StatusCode 302 (FOUND).
     * - ResponseEntity with StatusCode 404 (NOT_FOUND) if the course doesn't exist.
     */
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


    /**
     * Rest Path for GET-Request: "localhost:8081/api/course/title/{title}"
     * Method finds the course with a specific title in the database and checks if the wanted course actually exists.
     *
     * @param title - Title (String) of the wanted course
     * @return - ResponseBody incl. the wanted course, a pos. response message and StatusCode 302 (FOUND).
     * - ResponseBody incl. null for the course, errorMessage stating the wanted course doesn't exist and StatusCode 404 (NOT_FOUND)
     */
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


    /**
     * Rest Path for GET-Request: "localhost:8081/api/course/ongoing"
     * Method finds all ongoing courses at a specific date and returns them as a set.
     *
     * @param getCourseDTO - The RequestBody is JSON with the searchDate in the form (yyyy-mm-dd) i.e.:
     *                     {
     *                     "searchDate" : "2024-10-25"
     *                     }
     * @return - ResponseEntity with StatusCode 200 (OK). Response includes Set<Course>
     */
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


    /**
     * Rest Path for GET-Request: "localhost:8081/api/course/available"
     * Method finds all available courses after a specific date and returns them as a set.
     *
     * @param getCourseDTO - The RequestBody is JSON with the searchDate in the form (yyyy-mm-dd) i.e.:
     *      *                     {
     *      *                     "searchDate" : "2024-10-25"
     *      *                     }
     * @return - ResponseEntity with StatusCode 200 (OK). Response includes Set<Course>
     */
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


    /**
     * Rest Path for GET-Request: "localhost:8081/api/course/allafter"
     * Method finds all courses that start after a specific date and returns them as a set.
     *
     * @param getCourseDTO - The RequestBody is JSON with the searchDate in the form (yyyy-mm-dd) i.e.:
     *                     {
     *                     "searchDate" : "2024-10-25"
     *                     }
     *
     * @return - ResponseEntity with StatusCode 200 (OK). Response includes Set<Course>
     */
    @GetMapping("/allafter")
    public ResponseEntity<Set<Course>> getAllCoursesAfterDate (
            @RequestBody
            GetCourseDTO getCourseDTO) {

        // CHECK IF INPUT IS RIGHT DATE-FORMAT:

       // if (getCourseDTO.getSearchDate().) {


        List<Course> allCourses = courseRepository.findAll();
        Set<Course> futureCourses = new HashSet<>();

        for (Course course : allCourses) {
            if (course.getStartDate().isAfter(getCourseDTO.getSearchDate())) {
                futureCourses.add(course);
            }
        }

        return ResponseEntity.ok(futureCourses);
    }


}
