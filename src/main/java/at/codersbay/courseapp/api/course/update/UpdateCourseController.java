package at.codersbay.courseapp.api.course.update;

import at.codersbay.courseapp.api.ResponseBody;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import at.codersbay.courseapp.api.course.create.CreateCourseService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.OptionalLong;

@RestController
@RequestMapping("/api/course/")
public class UpdateCourseController {

    @Autowired
    private UpdateCourseService updateCourseService;

    @Autowired
    private CourseRepository courseRepository;


    /**
     * Rest Path for PUT-Request: "localhost:8081/api/course/{id}"
     * Method finds the course of a specific id in the database and updates it with the correct data provided in the RequestBody.
     * RespondMessages and StatusCodes are returned if data are incorrect.
     *
     * @param id              - Id (Long) of the wanted course
     * @param updateCourseDTO - The RequestBody should be a JSON object including one or multiple of the following parameters:
     *                        title (String) - Title of the course,
     *                        description (String) - Course description,
     *                        maxParticipants (int) - Maximum number of Participants of the course
     *                          (the general maximum Number (generalMaxCourseParticipants) can be changed in CreateCourseService-class,
     *                          default-value = 50 ),
     *                        startDate (LocalDate) - Date a course starts,
     *                        endDate (LocalDate) - Date a course ends
     * @return - ResponseBody incl. the complete updated course, a message, StatusCode (200, if successful)
     * possible Exceptions and StatusCodes (course is NULL in ResponseBody):
     * - No RequestBody: errorMessage, StatusCode 204 (NO_CONTENT)
     * - Course doesn't exist in database: errorMessage, StatusCode 404 (NOT_FOUND)
     * - Number of Participants exceeded_ errorMessage, StatusCode 409 (CONFLICT)
     * - EndDate is before StartDate: errorMessage, StatusCode 400 (BAD_REQUEST)
     * - Course could not be updated in database: errorMessage, StatusCode 500 (INTERNAL_SERVER_ERROR)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> updateCourseById(
            @PathVariable
            Long id,
            @RequestBody
            UpdateCourseDTO updateCourseDTO
    ) {
        CourseResponseBody courseResponseBody = new CourseResponseBody();


        //CHECK IF REQUESTBODY IS EMPTY:
        if (updateCourseDTO == null) {
            courseResponseBody.addErrorMessage("No Request Body.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.NO_CONTENT);
        }


        // CHECK IF WANTED COURSE EXISTS IN DB:
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (!optionalCourse.isPresent()) {
            courseResponseBody.addErrorMessage("The course with id " + id + " does not exist");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.NOT_FOUND);
        }

        Course updatedCourse = optionalCourse.get();

        if (!StringUtils.isEmpty(updateCourseDTO.getTitle())) {
            updatedCourse.setTitle(updateCourseDTO.getTitle());
        }
        if (!StringUtils.isEmpty(updateCourseDTO.getDescription())) {
            updatedCourse.setDescription((updateCourseDTO.getDescription()));
        }


        // CHECK IF MaxParticipants ARE EXCEEDED:
        if (!ObjectUtils.isEmpty(updateCourseDTO.getMaxParticipants())
                && updateCourseDTO.getMaxParticipants() > CreateCourseService.generalMaxCourseParticipants) {
            courseResponseBody.addErrorMessage("Maximum participants exceeded (" + CreateCourseService.generalMaxCourseParticipants + ").");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.CONFLICT);
        }

        if (!ObjectUtils.isEmpty(updateCourseDTO.getMaxParticipants())
                && updateCourseDTO.getMaxParticipants() > 0) {
            updatedCourse.setMaxParticipants(updateCourseDTO.getMaxParticipants());
        }


        // CHECK IF StartDate IS BEFORE EndDate:
        if (!ObjectUtils.isEmpty(updateCourseDTO.getStartDate())
                && !ObjectUtils.isEmpty(updateCourseDTO.getEndDate())
                && updateCourseDTO.getStartDate().isAfter(updateCourseDTO.getEndDate())) {
            courseResponseBody.addErrorMessage("StartDate cannot be after endDate.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.BAD_REQUEST);
        }

        if (!ObjectUtils.isEmpty(updateCourseDTO.getStartDate())
                && ObjectUtils.isEmpty(updateCourseDTO.getEndDate())
                && updateCourseDTO.getStartDate().isAfter(updatedCourse.getEndDate())) {
            courseResponseBody.addErrorMessage("StartDate cannot be after endDate (" + updatedCourse.getEndDate() + ").");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.BAD_REQUEST);
        }

        if (ObjectUtils.isEmpty(updateCourseDTO.getStartDate())
                && !ObjectUtils.isEmpty(updateCourseDTO.getEndDate())
                && updatedCourse.getStartDate().isAfter(updateCourseDTO.getEndDate())) {
            courseResponseBody.addErrorMessage("EndDate cannot be before startDate (" + updatedCourse.getStartDate() + ").");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.BAD_REQUEST);
        }


        if (!ObjectUtils.isEmpty(updateCourseDTO.getStartDate())) {
            updatedCourse.setStartDate(updateCourseDTO.getStartDate());
        }

        if (!ObjectUtils.isEmpty(updateCourseDTO.getEndDate())) {
            updatedCourse.setEndDate(updateCourseDTO.getEndDate());
        }

        this.courseRepository.save(updatedCourse);

        optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            courseResponseBody.addErrorMessage("The course with the id " + id + " could not be updated.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        courseResponseBody.addMessage("The course with id " + id + " was updated.");
        courseResponseBody.setCourse(updatedCourse);
        return new ResponseEntity<>(courseResponseBody, HttpStatus.OK);

    }

}
