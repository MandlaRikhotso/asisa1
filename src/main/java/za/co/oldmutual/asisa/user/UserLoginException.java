package za.co.oldmutual.asisa.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserLoginException extends Exception {

  public UserLoginException() {
    super();
  }

  public UserLoginException(String userId) {
    super(userId);
  }
}
