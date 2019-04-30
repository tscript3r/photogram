package pl.tscript3r.photogram2.exceptions.services;

import pl.tscript3r.photogram2.exceptions.PhotogramException;

public class UserNotFoundPhotogramException extends PhotogramException {

    public UserNotFoundPhotogramException(String message) {
        super(message);
    }

}
