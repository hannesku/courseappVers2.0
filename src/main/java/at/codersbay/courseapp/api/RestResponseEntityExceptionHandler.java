package at.codersbay.courseapp.api;

import at.codersbay.courseapp.api.user.exceptions.EmailAlreadyExistsExeption;
import at.codersbay.courseapp.api.user.exceptions.EmailIsNoEmailException;
import at.codersbay.courseapp.api.user.exceptions.IdIsEmptyException;
import at.codersbay.courseapp.api.user.exceptions.PasswordInsecureExeption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {IdIsEmptyException.class,
            EmailIsNoEmailException.class,
            PasswordInsecureExeption.class,
            EmailAlreadyExistsExeption.class})
    protected ResponseEntity<Object> handleConflict(
            Exception ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
