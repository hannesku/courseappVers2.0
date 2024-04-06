package at.codersbay.courseapp.api.user.update;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.UserResponseBody;
import at.codersbay.courseapp.api.user.exceptions.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/user/")
public class UpdateUserController {

    @Autowired
    private UserRepository userRepository;

    @PatchMapping("/")
    public ResponseEntity<UserResponseBody> updateUser (
            @RequestBody
            UpdateUserDTO updateUserDTO) throws EmailAlreadyExistsExeption, EmailIsNoEmailException,
            PasswordInsecureExeption {

        UserResponseBody userResponseBody = new UserResponseBody();

       // CHECK IF REQUESTBODY = EMPTY
        if (ObjectUtils.isEmpty(updateUserDTO.getId())) {
            throw new IdIsEmptyException("Id is missing in Requestbody.");
        } else if (StringUtils.isEmpty(updateUserDTO.getEmail()) && StringUtils.isEmpty(updateUserDTO.getPassword())) {
            userResponseBody.addErrorMessage("No update-data in requestbody.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.NO_CONTENT);
        }

        // CHECK IF USER EXISTS
        Optional<User> optionalUser = userRepository.findById(updateUserDTO.getId());
        User updatetedUser = null;
        if (optionalUser.isEmpty()) {
            userResponseBody.addErrorMessage("The user with id " + updateUserDTO.getId() + " doesn't exist.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.NOT_FOUND);
        } else  {
            updatetedUser = optionalUser.get();
        }

        // VALIDATE & UPDATE EMAIL
        String regexPatternEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if (!StringUtils.isEmpty(updateUserDTO.getEmail()) && userRepository.findByEmail(updateUserDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsExeption("The email-adress " + updateUserDTO.getEmail() + " already exists.");
        } else if (!StringUtils.isEmpty(updateUserDTO.getEmail()) && !updateUserDTO.getEmail().matches(regexPatternEmail)) {
            throw new EmailIsNoEmailException("The email has no valuable format (name@abcd.efg).");
        } else {
            updatetedUser.setEmail(updateUserDTO.getEmail());
        }

        // VALIDATE & UPDATE PASSWORD:
        if (!StringUtils.isEmpty(updateUserDTO.getPassword()) &&
                updateUserDTO.getPassword().length()<8 ||
                !StringUtils.isMixedCase(updateUserDTO.getPassword()) ||
                !updateUserDTO.getPassword().matches(".*\\d.*")) {
            throw new PasswordInsecureExeption("The password is insecure. Necessary criteria: min.8 char., mixed case, contains also numbers.");
        } else {
            updatetedUser.setPassword(updateUserDTO.getPassword());
        }


        this.userRepository.save(updatetedUser);

        userResponseBody.addMessage("The user with id " + updateUserDTO.getId() + " was updated.");
        userResponseBody.setUser(updatetedUser);
        return new ResponseEntity<>(userResponseBody, HttpStatus.OK);

    }


}
