package at.codersbay.courseapp.api.user.get;


import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.UserResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class GetUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById (
            @PathVariable
            Long id) {

        UserResponseBody userResponseBody = new UserResponseBody();

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            userResponseBody.addErrorMessage("The user with id " + id + " doesn't exist.");
            ResponseEntity response = new ResponseEntity(userResponseBody, HttpStatus.NOT_FOUND);
            return response;
        }

        User userById = optionalUser.get();
        return ResponseEntity.ok(userById);
    }


}
