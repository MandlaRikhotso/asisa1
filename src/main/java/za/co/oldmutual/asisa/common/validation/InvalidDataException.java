package za.co.oldmutual.asisa.common.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidDataException extends Exception {

  public InvalidDataException() {
    super();
  }

}
