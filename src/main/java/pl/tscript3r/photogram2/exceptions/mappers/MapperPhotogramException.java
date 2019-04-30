package pl.tscript3r.photogram2.exceptions.mappers;

import pl.tscript3r.photogram2.exceptions.PhotogramException;

public class MapperPhotogramException extends PhotogramException {

    public MapperPhotogramException(String message) {
        super(message);
    }

    public MapperPhotogramException(String message, Throwable cause) {
        super(message, cause);
    }

}
