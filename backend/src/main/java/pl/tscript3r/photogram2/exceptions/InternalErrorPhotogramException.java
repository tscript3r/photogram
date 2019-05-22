package pl.tscript3r.photogram2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorPhotogramException extends PhotogramException {

    public InternalErrorPhotogramException(String message) {
        super(message);
    }

    public InternalErrorPhotogramException(String message, Throwable cause) {
        super(message, cause);
    }

}
