package pl.tscript3r.photogram2.exceptions.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.tscript3r.photogram2.exceptions.PhotogramException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestPhotogramException extends PhotogramException {

    public BadRequestPhotogramException(String message) {
        super(message);
    }

}
