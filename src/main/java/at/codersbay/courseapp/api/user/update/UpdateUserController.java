package at.codersbay.courseapp.api.user.update;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.UserResponseBody;
import at.codersbay.courseapp.api.user.create.CreateUserService;
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


    /**
     * @param updateUserEmailDTO
     * @return
     * @throws EmailAlreadyExistsExeption
     * @throws EmailIsNoEmailException
     * @throws EmailIsEmptyException
     */
    @PatchMapping("/email/")
    public ResponseEntity<UserResponseBody> updateUserEmail(
            @RequestBody
            UpdateUserEmailDTO updateUserEmailDTO)
            throws EmailAlreadyExistsExeption, EmailIsNoEmailException, EmailIsEmptyException {

        UserResponseBody userResponseBody = new UserResponseBody();

        // CHECK IF REQUESTBODY = EMPTY
        if (ObjectUtils.isEmpty(updateUserEmailDTO.getId())) {
            throw new IdIsEmptyException("Id is missing in requestbody.");
        } else if (StringUtils.isEmpty(updateUserEmailDTO.getEmail())) {
            throw new EmailIsEmptyException("No email in requestbody.");
//          userResponseBody.addErrorMessage("No email in requestbody.");
//          return new ResponseEntity<>(userResponseBody, HttpStatus.BAD_REQUEST);
        }

        // CHECK IF USER EXISTS
        Optional<User> optionalUser = userRepository.findById(updateUserEmailDTO.getId());
        User updateUser = null;
        if (optionalUser.isEmpty()) {
            userResponseBody.addErrorMessage("The user with id " + updateUserEmailDTO.getId() + " doesn't exist.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.NOT_FOUND);
        } else {
            updateUser = optionalUser.get();
        }

        // VALIDATE & UPDATE EMAIL
        if (!StringUtils.isEmpty(updateUserEmailDTO.getEmail())
                && userRepository.findByEmail(updateUserEmailDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsExeption("The email-adress " + updateUserEmailDTO.getEmail() + " already exists.");
        } else if (!StringUtils.isEmpty(updateUserEmailDTO.getEmail())
                && !updateUserEmailDTO.getEmail().matches(CreateUserService.regexPatternEmail)) {
            throw new EmailIsNoEmailException("The email has no valuable format (name@abcd.efg).");
        } else {
            updateUser.setEmail(updateUserEmailDTO.getEmail());
        }

        this.userRepository.save(updateUser);

        userResponseBody.addMessage("The email of user " + updateUserEmailDTO.getId() + " was updated.");
        userResponseBody.setUser(updateUser);
        return new ResponseEntity<>(userResponseBody, HttpStatus.OK);

    }

    @PatchMapping("/password/")
    public ResponseEntity<UserResponseBody> updateUserPassword(
            @RequestBody
            UpdateUserPasswordDTO updateUserPasswordDTO)
            throws PasswordInsecureExeption, PasswordIsEmptyException {

        UserResponseBody userResponseBody = new UserResponseBody();

        // CHECK IF REQUESTBODY = EMPTY
        if (ObjectUtils.isEmpty(updateUserPasswordDTO.getId())) {
            throw new IdIsEmptyException("Id is missing in Requestbody.");
        } else if (StringUtils.isEmpty(updateUserPasswordDTO.getPassword())) {
//            throw new PasswordIsEmptyException("No password in requestbody.");
            userResponseBody.addErrorMessage("No password in requestbody.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.BAD_REQUEST);
        }

        // CHECK IF USER EXISTS
        Optional<User> optionalUser = userRepository.findById(updateUserPasswordDTO.getId());
        User updateUser = null;
        if (optionalUser.isEmpty()) {
            userResponseBody.addErrorMessage("The user with id " + updateUserPasswordDTO.getId() + " doesn't exist.");
            return new ResponseEntity<>(userResponseBody, HttpStatus.NOT_FOUND);
        } else {
            updateUser = optionalUser.get();
        }

        // VALIDATE & UPDATE PASSWORD:
        if (!StringUtils.isEmpty(updateUserPasswordDTO.getPassword()) &&
                updateUserPasswordDTO.getPassword().length() < CreateUserService.minPasswordLength ||
                !StringUtils.isMixedCase(updateUserPasswordDTO.getPassword()) ||
                !updateUserPasswordDTO.getPassword().matches(".*\\d.*")) {
            throw new PasswordInsecureExeption("The password is insecure. Necessary criteria: min. " + CreateUserService.minPasswordLength + " char., mixed case, contains also numbers.");
        } else {
            updateUser.setPassword(updateUserPasswordDTO.getPassword());
        }

        this.userRepository.save(updateUser);

        userResponseBody.addMessage("The password of user " + updateUserPasswordDTO.getId() + " was updated.");
        userResponseBody.setUser(updateUser);
        return new ResponseEntity<>(userResponseBody, HttpStatus.OK);

    }


}
