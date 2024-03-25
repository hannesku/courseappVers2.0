package at.codersbay.courseapp.data;

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

    @PostConstruct
    public void setup() {
        importUsers();
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

}
