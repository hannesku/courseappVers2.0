package at.codersbay.courseapp.api.course.update;

import at.codersbay.courseapp.api.ResponseBody;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.OptionalLong;

@RestController
@RequestMapping("/api/course/{id}")
public class UpdateCourseController {

    @Autowired
    private UpdateCourseService updateCourseService;

    @Autowired
    private CourseRepository courseRepository;

    @PutMapping
    public ResponseEntity<ResponseBody> updateCourseById (
            @PathVariable
            Long id,
            @RequestBody
            UpdateCourseDTO updateCourseDTO
    ) {
        CourseResponseBody courseResponseBody = new CourseResponseBody();

        if (updateCourseDTO == null){
            courseResponseBody.addErrorMessage("No Request Body.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.NO_CONTENT);
        }

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (!optionalCourse.isPresent()) {
            courseResponseBody.addErrorMessage("The course with id " + id + " does not exist");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.NOT_FOUND);
        }

        Course updatedCourse = optionalCourse.get();

        if (!StringUtils.isEmpty(updateCourseDTO.getTitle()) || !updateCourseDTO.getTitle().isEmpty()) {
            updatedCourse.setTitle(updateCourseDTO.getTitle());
        }
        if (!StringUtils.isEmpty(updateCourseDTO.getDescription()) || !updateCourseDTO.getDescription().isEmpty()) {
            updatedCourse.setDescription((updateCourseDTO.getDescription()));
        }
        if (updateCourseDTO.getMaxParticipants() > 0) {
            updatedCourse.setMaxParticipants(updateCourseDTO.getMaxParticipants());
        }

        this.courseRepository.save(updatedCourse);

        optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            courseResponseBody.addErrorMessage("The course with the id " + id + " could not be updated.");
            return new ResponseEntity<>(courseResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        courseResponseBody.addMessage("The course with id " + id + " was updated to:");
        courseResponseBody.setCourse(updatedCourse);
        return new ResponseEntity<>(courseResponseBody, HttpStatus.OK);

    }

@PutMapping("/bytitle")
    public ResponseEntity<ResponseBody> updateCourseByTitle (
            @RequestBody
            UpdateCourseDTO updateCourseDTO) {

    System.out.println("Title to update: " + updateCourseDTO.getTitle());

    CourseResponseBody courseResponseBody = new CourseResponseBody();

    // Check if Request Body (updateCourseDTO) is null.

    if (StringUtils.isEmpty(updateCourseDTO.getTitle())) {
        courseResponseBody.addErrorMessage("The title is empty.");
        return new ResponseEntity<>(courseResponseBody, HttpStatus.BAD_REQUEST);
    }

    Optional<Course> optionalCourse = courseRepository.findByTitle(updateCourseDTO.getTitle());

    if (!optionalCourse.isPresent()) {
        courseResponseBody.addErrorMessage("Can't find course with the title " + updateCourseDTO.getTitle());
        return new ResponseEntity<>(courseResponseBody, HttpStatus.NOT_FOUND);
    }

    Course updateCourse = this.updateCourseService.updateCourse(updateCourseDTO.getDescription(), updateCourseDTO.getMaxParticipants());
    courseResponseBody.addMessage("The course " + updateCourseDTO.getTitle() + " was updated to:");
    courseResponseBody.setCourse(updateCourse);
    return new ResponseEntity<>(courseResponseBody, HttpStatus.OK);


}


}