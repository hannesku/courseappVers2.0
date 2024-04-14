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
     * Rest Path - PATCH-Request: "localhost:8081/api/user/email/"
     * Method finds an existing user of a given id and updates the email with the value provided in the RequestBody.
     * The provided value for email is checked if it is in a plausible format and if it already exists.
     *
     * @param updateUserEmailDTO - The RequestBody should be a JSON object with the parameters:
     *                           id (Long) - userId of the wanted user.
     *                           e-mail (String) - unique email-address in correct email-format.
     *                           for instance: {
     *                                              "id" : 2,
     *                                              "email": "username@gmail.com"
     *                                          }
     * @return - ResponseBody incl. updated user, message, StatusCode 200 (OK)
     * in case the user doesn't exist: ResponseBody incl. no user, errorMessage, StatusCode 404 (NOT_FOUND)
     * other possible Exceptions:
     * @throws IdIsEmptyException          - no userId in RequestBody: errorMessage, StatusCode 409 (CONFLICT)
     * @throws EmailAlreadyExistsException - email already exists: errorMessage, StatusCode 409 (CONFLICT)
     * @throws EmailIsNoEmailException     - email has no valuable format: errorMessage, StatusCode 409 (CONFLICT)
     * @throws EmailIsEmptyException       - there is no email in the RequestBOdy: errorMessage, StatusCode 409 (CONFLICT)
     */
    @PatchMapping("/email/")
    public ResponseEntity<UserResponseBody> updateUserEmail(
            @RequestBody
            UpdateUserEmailDTO updateUserEmailDTO)
            throws IdIsEmptyException, EmailAlreadyExistsException, EmailIsNoEmailException, EmailIsEmptyException {

        UserResponseBody userResponseBody = new UserResponseBody();

        // CHECK IF REQUESTBODY = EMPTY
        if (ObjectUtils.isEmpty(updateUserEmailDTO.getId())) {
            throw new IdIsEmptyException("UserId ('id') is missing in requestbody.");
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
            throw new EmailAlreadyExistsException("The email-adress " + updateUserEmailDTO.getEmail() + " already exists.");
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


    /**
     * Rest Path - PATCH-Request: "localhost:8081/api/user/password/"
     * Method finds an existing user of a given id and updates the password with the value provided in the RequestBody.
     * The provided value for password is checked if it is considered secure.
     *
     * @param updateUserPasswordDTO - The RequestBody should be a JSON object with the parameters:
     *                                id (Long) - userId of the wanted user.
     *                                password (String) - "secure" password for the user. It is considered secure when it meets the following criteria:
     *                                      includes upper- and lowercase letter(s), includes number(s), min.length = minPasswordLength (default: 8 chars)
     *                                  for instance: {
     *                                                "id" : 2,
     *                                                "password": "NewPassword1234"
     *                                                }
     *
     * @return - ResponseBody incl. updated user, message, StatusCode 200 (OK)
     * in case the user doesn't exist: ResponseBody incl. no user, errorMessage, StatusCode 404 (NOT_FOUND)
     * other possible Exceptions:
     * @throws IdIsEmptyException           - no userId in RequestBody: errorMessage, StatusCode 409 (CONFLICT)
     * @throws PasswordInsecureException    - provided password is considered insecure: errorMessage, StatusCode 409 (CONFLICT)
     * @throws PasswordIsEmptyException     - there is no password in the RequestBOdy: errorMessage, StatusCode 409 (CONFLICT)
     */
    @PatchMapping("/password/")
    public ResponseEntity<UserResponseBody> updateUserPassword(
            @RequestBody
            UpdateUserPasswordDTO updateUserPasswordDTO)
            throws IdIsEmptyException, PasswordInsecureException, PasswordIsEmptyException {

        UserResponseBody userResponseBody = new UserResponseBody();

        // CHECK IF REQUESTBODY = EMPTY
        if (ObjectUtils.isEmpty(updateUserPasswordDTO.getId())) {
            throw new IdIsEmptyException("UserId ('id') is missing in requestbody.");
        } else if (StringUtils.isEmpty(updateUserPasswordDTO.getPassword())) {
            throw new PasswordIsEmptyException("No password in requestbody.");
//            userResponseBody.addErrorMessage("No password in requestbody.");
//            return new ResponseEntity<>(userResponseBody, HttpStatus.BAD_REQUEST);
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
            throw new PasswordInsecureException("The password is insecure. Necessary criteria: min. " + CreateUserService.minPasswordLength + " char., mixed case, contains also numbers.");
        } else {
            updateUser.setPassword(updateUserPasswordDTO.getPassword());
        }

        this.userRepository.save(updateUser);

        userResponseBody.addMessage("The password of user " + updateUserPasswordDTO.getId() + " was updated.");
        userResponseBody.setUser(updateUser);
        return new ResponseEntity<>(userResponseBody, HttpStatus.OK);

    }


}
