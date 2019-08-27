package pl.tscript3r.photogram.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestPhotogramException extends PhotogramException {

    public BadRequestPhotogramException(String message) {
        super(message);
    }

}
