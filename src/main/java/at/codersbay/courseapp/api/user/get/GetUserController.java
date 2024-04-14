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


    /**
     * Rest Path - GET-Request: "localhost:8081/api/user/"
     * Method finds all users in the database.
     *
     * @return - ResponseEntity with StatusCode 200 (OK). The response includes a List<User>.
     */
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(allUsers);
    }


    /**
     * Rest Path - GET-Request: "localhost:8081/api/user/{id}"
     * Method finds the user of a specific id in the database and checks if the wanted user actually exists.
     *
     * @param id - Id (Long) of the wanted user
     * @return - ResponseEntity with StatusCode 200 (OK). The response includes the wanted user as JSON-Object.
     * - if the user doesn't exist in the database: ResponseBody with errorMessage and StatusCode 302 (NOT_FOUND)
     */
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
