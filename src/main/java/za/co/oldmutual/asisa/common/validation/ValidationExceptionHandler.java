package za.co.oldmutual.asisa.common.validation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import za.co.oldmutual.asisa.user.UserLoginException;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

  static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
  
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    BindingResult bindingResult = ex.getBindingResult();
    List<FieldErrorBean> fieldErrors = null;
    List<GlobalErrorBean> globalErrors = null;
    if (bindingResult.hasFieldErrors()) {
      fieldErrors = new ArrayList<>();
      for (FieldError fieldError : bindingResult.getFieldErrors()) {
        fieldErrors.add(new FieldErrorBean(fieldError.getCode(), fieldError.getField(),
            fieldError.getRejectedValue().toString(), fieldError.getDefaultMessage()));
      }
    }
    if (bindingResult.hasGlobalErrors()) {
      globalErrors = new ArrayList<>();
      for (ObjectError globalError : bindingResult.getGlobalErrors()) {
        globalErrors
            .add(new GlobalErrorBean(globalError.getCode(), globalError.getDefaultMessage()));
      }
    }
    return new ResponseEntity<>(new ValidationExceptionBean(globalErrors, fieldErrors),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = ResourceNotFoundException.class)
  public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException e,
      WebRequest req) {
    List<GlobalErrorBean> globalErrors = new ArrayList<>();
    globalErrors.add(new GlobalErrorBean(INTERNAL_SERVER_ERROR, "Resource not found"));
    return new ResponseEntity<>(new ValidationExceptionBean(globalErrors, null),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = ResourceUpdationFailedException.class)
  public ResponseEntity<Object> resourceUpdationFailedException(ResourceUpdationFailedException e,
      WebRequest req) {
    List<GlobalErrorBean> globalErrors = new ArrayList<>();
    globalErrors.add(new GlobalErrorBean(INTERNAL_SERVER_ERROR, "Resource updation failed"));
    return new ResponseEntity<>(new ValidationExceptionBean(globalErrors, null),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = InvalidDataException.class)
  public ResponseEntity<Object> invalidDataException(InvalidDataException e, WebRequest req) {
    List<GlobalErrorBean> globalErrors = new ArrayList<>();
    globalErrors.add(new GlobalErrorBean(INTERNAL_SERVER_ERROR, "Invalid data"));
    return new ResponseEntity<>(new ValidationExceptionBean(globalErrors, null),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> invalidRequestException(MethodArgumentTypeMismatchException e,
      WebRequest req) {
    List<GlobalErrorBean> globalErrors = new ArrayList<>();
    globalErrors.add(new GlobalErrorBean(INTERNAL_SERVER_ERROR, "Invalid request"));
    return new ResponseEntity<>(new ValidationExceptionBean(globalErrors, null),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = UserLoginException.class)
  public ResponseEntity<Object> userLoginException(UserLoginException e, WebRequest req) {
    List<GlobalErrorBean> globalErrors = new ArrayList<>();
    globalErrors.add(new GlobalErrorBean(INTERNAL_SERVER_ERROR, e.getMessage()));
    return new ResponseEntity<>(new ValidationExceptionBean(globalErrors, null),
        HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(value = AccessDeniedException.class)
  public ResponseEntity<Object> accessDeniedException(AccessDeniedException e,
      WebRequest req) {
    List<GlobalErrorBean> globalErrors = new ArrayList<>();
    globalErrors.add(new GlobalErrorBean("403", "Access is denied"));
    return new ResponseEntity<>(new ValidationExceptionBean(globalErrors, null),
        HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = SpecialCharactersFoundException.class)
  public ResponseEntity<Object> specialCharactersFoundException(SpecialCharactersFoundException e, WebRequest req) {
	    List<GlobalErrorBean> globalErrors = new ArrayList<>();
	    globalErrors.add(new GlobalErrorBean(INTERNAL_SERVER_ERROR, "SPECIAL CHARS FOUND"));
	    return new ResponseEntity<>(new ValidationExceptionBean(globalErrors, null),
	        HttpStatus.BAD_REQUEST);
	  }

}
