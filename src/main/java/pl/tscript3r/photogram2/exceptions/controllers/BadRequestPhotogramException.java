package pl.tscript3r.photogram2.exceptions.controllers;

import pl.tscript3r.photogram2.exceptions.PhotogramException;

public class BadRequestPhotogramException extends PhotogramException {

    public BadRequestPhotogramException(String message) {
        super(message);
    }

}
