package pl.tscript3r.photogram.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenPhotogramException extends PhotogramException {

    public ForbiddenPhotogramException(String message) {
        super(message);
    }

}
