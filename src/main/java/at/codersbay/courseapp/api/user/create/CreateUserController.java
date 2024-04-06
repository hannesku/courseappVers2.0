package at.codersbay.courseapp.api.user.create;

import at.codersbay.courseapp.api.ResponseBody;
import at.codersbay.courseapp.api.booking.BookingRepository;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.UserResponseBody;
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

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/")
    public ResponseEntity<UserResponseBody> createNewUser (
            @RequestBody
            CreateUserDTO createUserDTO) {

        UserResponseBody userResponseBody = new UserResponseBody();

        if (createUserDTO == null) {
            userResponseBody.addErrorMessage("The Requestbody is empty.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.BAD_REQUEST);
        }

        User newUser = null;

        try {
            newUser = this.createUserService.createUser(
                    createUserDTO.getUsername(),
                    createUserDTO.getPassword(),
                    createUserDTO.getEmail());
        } catch (EmailAlreadyExistsExeption |
                EmailIsEmptyException |
                EmailIsNoEmailException |
                PasswordInsecureExeption |
                PasswordIsEmptyException |
                UserAlreadyExistsExeption |
                UsernameIsEmptyException exception) {
            userResponseBody.addErrorMessage(exception.getMessage());
            return new ResponseEntity<>(userResponseBody, HttpStatus.BAD_REQUEST);
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
