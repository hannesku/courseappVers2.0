package at.codersbay.courseapp.data;

import at.codersbay.courseapp.api.booking.Booking;
import at.codersbay.courseapp.api.booking.BookingRepository;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @PostConstruct
    public void setup() {
        importUsers();
        importCourses();
        createInitialBookings();
    }

    public void importUsers() {
        List<User> allUsers = this.userRepository.findAll();

        if(allUsers.size() > 0) {
            return;
        }

        User initialUser01 = new User();
        initialUser01.setUsername("maxmustermann");
        initialUser01.setPassword("pwmax");
        initialUser01.setEmail("max.mustermann@gmail.com");
        this.userRepository.save(initialUser01);

        User initialUser02 = new User();
        initialUser02.setUsername("erikamusterfrau");
        initialUser02.setPassword("pwerika");
        initialUser02.setEmail("erika.musterfrau@gmail.at");
        this.userRepository.save(initialUser02);

        User initialUser03 = new User();
        initialUser03.setUsername("wissensdurst");
        initialUser03.setPassword("pwwissen");
        initialUser03.setEmail("wissendsdurst@gmx.net");
        this.userRepository.save(initialUser03);

    }

    public void importCourses() {
        List<Course> allCourses = this.courseRepository.findAll();

        if (allCourses.size() > 0) {
            return;
        }

        Course java = new Course();
        java.setTitle("Java");
        java.setDescription("Description for Java Course");
        // java.setDescription("From mastering the fundamentals and object-oriented principles to hands-on experience with development tools, GUI, database connectivity, web development, and frameworks, a well-structured Java course equips learners with the skills needed in the dynamic field of software development.");
        java.setMaxParticipants(8);
        this.courseRepository.save(java);

        Course springBoot = new Course();
        springBoot.setTitle("Spring Boot");
        springBoot.setDescription("Text about the Spring Boot Course.");
        // springBoot.setDescription("Spring Boot courses offered through Coursera equip learners with knowledge in using the framework on the Google Cloud Platform; programming with Java in Spring Boot; using object-oriented design techniques; and more.");
        springBoot.setMaxParticipants(10);
        this.courseRepository.save(springBoot);

        Course python = new Course();
        python.setTitle("Python");
        python.setDescription("What students are going to learn in the python-course.");
        //python.setDescription("Students are introduced to core programming concepts like data structures, conditionals, loops, variables, and functions. This course includes an overview of the various tools available for writing and running Python, and gets students coding quickly.");
        python.setMaxParticipants(15);
        this.courseRepository.save(python);

    }

    public void createInitialBookings() {

        List<Booking> allBookings = this.bookingRepository.findAll();
        if (allBookings.size()>0) {
            return;
        }


        Booking firstBooking = new Booking();
        firstBooking.setUser(userRepository.findByUsername("maxmustermann").get());
        firstBooking.setCourse(courseRepository.findByTitleIgnoreCase("Java").get());
        bookingRepository.save(firstBooking);

        Booking secondBooking = new Booking();
        secondBooking.setUser(userRepository.findByUsername("maxmustermann").get());
        secondBooking.setCourse(courseRepository.findByTitleIgnoreCase("Python").get());
        bookingRepository.save(secondBooking);

        Booking thirdBooking = new Booking();
        thirdBooking.setUser(userRepository.findByUsername("wissensdurst").get());
        thirdBooking.setCourse(courseRepository.findByTitleIgnoreCase("Spring Boot").get());
        bookingRepository.save(thirdBooking);




    }

}
