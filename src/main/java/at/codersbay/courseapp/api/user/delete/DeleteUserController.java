package at.codersbay.courseapp.api.user.delete;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.UserResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user/")
public class DeleteUserController {

    @Autowired
    private UserRepository userRepository;

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseBody> deleteUserById (
            @PathVariable
            Long id) {

        UserResponseBody userResponseBody = new UserResponseBody();

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            userResponseBody.addErrorMessage("The User with the id " + id + " doesn't exist.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.NOT_FOUND);
        }

        userRepository.deleteById(id);

        optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            userResponseBody.addErrorMessage("Could not delete user with id " + id + " .");
            return new ResponseEntity<>(userResponseBody, HttpStatus.BAD_REQUEST);
        }
            userResponseBody.addMessage("User with id " + id + " deleted.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.OK);


    }

}
