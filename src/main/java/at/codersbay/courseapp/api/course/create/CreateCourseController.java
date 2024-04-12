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


    /**
     * Rest Path for POST-Request: "localhost:8081/api/course/"
     * Method creates a new course and saves it to the database.
     * A new id number in the format Long is generated for the new course.
     * ErrorMessages are returned for the following Exceptions:
     * - Missing value in the RequestBody
     * - Number of participants too big
     * - Start Date of the course is after its End Date
     * - The course exists already in the database
     *
     * @param createCourseDTO - The RequestBody should be a JSON object with the parameters:
     *                        title (String) - Title of the course,
     *                        description (String) - Course description,
     *                        maxParticipants (int) - Maximum number of Participants of the course
     *                        (the general maximum Number (generalMaxCourseParticipants) can be changed in CreateCourseService-class,
     *                        default-value = 50 ),
     *                        startDate (LocalDate) - Date a course starts,
     *                        endDate (LocalDate) - Date a course ends
     *
     * @return - ResponseBody incl. the newly created and saved course, a message or errorMessage and StatusCode
     * - Everything correct: course, pos. message, StatusCode 200 (OK)
     * possible Exceptions and StatusCodes (course is NULL in ResponseBody):
     * - Title is missing in RequestBody (TitleIsEmptyException): errorMessage, StatusCode 400 (BAD_REQUEST)
     * - Description is missing in RequestBody (DescriptionIsEmptyException): errorMessage, StatusCode 400 (BAD_REQUEST)
     * - Number of Participants are missing in RequestBody (NumberOfParticipantsException): errorMessage, StatusCode 400 (BAD_REQUEST)
     * - Number of Participants greater than generalMaxCourseParticipants (default=50) (NumberOfParticipantsException): errorMessage, StatusCode 400 (BAD_REQUEST)
     * - StartDate or EndDate are missing in RequestBody (DateIsEmptyException): errorMessage, StatusCode 400 (BAD_REQUEST)
     * - EndDate is before StartDate (IncorrectDateException): errorMessage, StatusCode 400 (BAD_REQUEST)
     * - The course exists already in database: errorMessage, StatusCode 409 (CONFLICT)
     * - The course could not be saved in database: errorMessage, StatusCode 500 (INTERNAL_SERVER_ERROR)
     */
    @PostMapping
    public ResponseEntity<ResponseBody> createNewCourse(
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
                 DateIsEmptyException |
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
