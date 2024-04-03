package at.codersbay.courseapp.api.user.create;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser (String username, String password, String email)
    //    throws
    {
    User newUser = new User(username, password, email);
    return userRepository.save(newUser);
    }
}
