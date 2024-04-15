package at.codersbay.courseapp.api.user.create;

import at.codersbay.courseapp.api.user.*;
import at.codersbay.courseapp.api.user.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/")
public class CreateUserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreateUserService createUserService;


    /**
     * Rest Path - POST-Request: "localhost:8081/api/user/"
     * Method creates a new user and saves it to the database.
     * A new id-number (Long) is generated for the new user.
     * The input in the RequestBody is checked and errorMessages are returned for the following Exceptions:
     * - UserName, password or email are missing in RequestBody.
     * - The provided email has no valuable format.
     * - Username already exists in database (NOTE: case insensitivity for all chars of the username!)
     * - Email already exists for another user.
     * - The provided password is considered insecure (necessary requirements:
     *      - it includes upper- and lowercase letters
     *      - at least one number is included
     *      - password is equal or longer to the minimum length of password
     *          minPasswordLength (int, default-value = 8) can be changed in CreateUserService-class
     *
     * @param createUserDTO - The RequestBody should be a JSON object with the parameters:
     *                      username (String) - Name of user,
     *                      password (String) - "secure" password for the user. It is considered secure when it meets the following criteria:
     *                          includes upper- and lowercase letter(s), includes number(s), min.length = minPasswordLength (default: 8 chars)
     *                      e-mail (String) - unique email-address in correct format.
     *
     * @return - ResponseBody incl. the newly created and saved user, a message / errorMessage and StatusCode
     *  - Everything correct: user, pos. message, StatusCode 200 (OK)
     *  possible Exceptions and StatusCodes (user is NULL in ResponseBody):
     *  - UserName is missing in RequestBody (UsernameIsEmptyException): errorMessage, StatusCode 400 (BAD_REQUEST)
     *  - Password is missing in RequestBody (PasswordIsEmptyException): errorMessage, StatusCode 400 (BAD_REQUEST)
     *  - Email is missing in RequestBody (UEmailIsEmptyException): errorMessage, StatusCode 400 (BAD_REQUEST)
     *  - Email has no valuable format (EmailIsNoEmailException); errorMessage, StatusCode 400 (BAD_REQUEST)
     *  - Username already exists in the database (UserAlreadyExistsException); errorMessage, StatusCode 409 (CONFLICT)
     *  - Email already exists in the database (EmailAlreadyExistsException); errorMessage, StatusCode 409 (CONFLICT)
     *  - Provided Password is considered insecure (PasswordInsecureException); errorMessage, StatusCode 400 (BAD_REQUEST)
     *  - The new user could not be saved to the database: errorMessage, StatusCode 500 (INTERNAL_SERVER_ERROR)
     */
    @PostMapping("/")
    public ResponseEntity<UserResponseBody> createNewUser (
            @RequestBody
            CreateUserDTO createUserDTO) {

        UserResponseBody userResponseBody = new UserResponseBody();

        /*
        if (createUserDTO == null) {
            userResponseBody.addErrorMessage("The Requestbody is empty.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.BAD_REQUEST);
        }
         */

        User newUser = null;

        try {
            newUser = this.createUserService.createUser(
                    createUserDTO.getUsername(),
                    createUserDTO.getPassword(),
                    createUserDTO.getEmail());
        } catch (EmailIsEmptyException |
                 EmailIsNoEmailException |
                 PasswordInsecureException |
                 PasswordIsEmptyException |
                 UsernameIsEmptyException exception) {
            userResponseBody.addErrorMessage(exception.getMessage());
            return new ResponseEntity<>(userResponseBody, HttpStatus.BAD_REQUEST);
        } catch (UserAlreadyExistsException | EmailAlreadyExistsException alreadyExistsException) {
            userResponseBody.addErrorMessage(alreadyExistsException.getMessage());
            return new ResponseEntity<>(userResponseBody, HttpStatus.CONFLICT);
        }

        if (!userRepository.findByUsernameIgnoreCase(createUserDTO.getUsername()).isPresent()) {
            userResponseBody.addErrorMessage("The user couldn't be saved.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        userResponseBody.setUser(newUser);
        userResponseBody.addMessage("The new user was created and saved.");
        return new ResponseEntity<>(userResponseBody, HttpStatus.OK);

    }

}
