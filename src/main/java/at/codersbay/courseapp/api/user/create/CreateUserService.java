package at.codersbay.courseapp.api.user.create;

import at.codersbay.courseapp.api.user.*;
import at.codersbay.courseapp.api.user.exceptions.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    private static final int minPasswordLength = 8;
    private static final String regexPatternEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    private UserRepository userRepository;


    public User createUser(String username, String password, String email)
            throws EmailIsEmptyException,
            EmailIsNoEmailException,
            PasswordIsEmptyException,
            UsernameIsEmptyException,
            UserAlreadyExistsExeption,
            EmailAlreadyExistsExeption,
            PasswordInsecureExeption {

        if (StringUtils.isEmpty(username)) {
            throw new UsernameIsEmptyException("Username is missing.");
        } else if (StringUtils.isEmpty(password)) {
            throw new PasswordIsEmptyException("Password is missing.");
        } else if (StringUtils.isEmpty(email)) {
            throw new EmailIsEmptyException("Email is missing.");
        } else if (!email.matches(regexPatternEmail)) {
            throw new EmailIsNoEmailException("The email has no valuable format (name@abcd.efg).");
        } else if (userRepository.findByUsernameIgnoreCase(username).isPresent()) {
            throw new UserAlreadyExistsExeption("The username " + username + " already exists.");
        } else if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsExeption("The email-adress " + email + " already exists.");
        } else if (password.length() < minPasswordLength || !StringUtils.isMixedCase(password) || !password.matches(".*\\d.*")) {
            throw new PasswordInsecureExeption("The password is insecure. Necessary criteria: min. " + minPasswordLength + " char., mixed case, contains also numbers.");
        }

        User newUser = new User(username, password, email);
        return userRepository.save(newUser);
    }
}
