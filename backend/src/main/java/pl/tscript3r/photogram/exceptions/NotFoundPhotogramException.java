package pl.tscript3r.photogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundPhotogramException extends PhotogramException {

    public NotFoundPhotogramException(String message) {
        super(message);
    }

}
