package at.codersbay.courseapp.api.course;

import at.codersbay.courseapp.api.ResponseBody;

public class CourseResponseBody extends ResponseBody {

    private Course course;

    public CourseResponseBody() {

    }

    public CourseResponseBody(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
