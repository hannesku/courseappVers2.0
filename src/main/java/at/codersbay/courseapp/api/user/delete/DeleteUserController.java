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


    /**
     * Rest Path - DELETE-Rewuest: "localhost:8081/api/user/{id}"
     * Method finds the user of a specific id in the database, checks if it exists and if it does removes it from the database.
     * NOTE: If bookings of the wanted user exist, they will also be removed from the bookings-List.
     *
     * @param id - userId (Long) of wanted user
     * @return - ResponseBody incl. confirmation message, StatusCode 200 (OK)
     *  - ResponseBody incl errorMessage if the user doesn't exist in the database, StatusCode 404 (NOT_FOUND)
     *  - ResponseBody incl errorMessage if the user exists but couldn't be deleted, StatusCode 503 (SERVICE_UNAVAILABLE)
     */
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
            return new ResponseEntity<>(userResponseBody, HttpStatus.SERVICE_UNAVAILABLE);
        }
            userResponseBody.addMessage("User with id " + id + " deleted.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.OK);


    }

}
