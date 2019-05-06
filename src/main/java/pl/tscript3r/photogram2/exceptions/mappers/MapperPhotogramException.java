package pl.tscript3r.photogram2.exceptions.mappers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.tscript3r.photogram2.exceptions.PhotogramException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class MapperPhotogramException extends PhotogramException {

    public MapperPhotogramException(String message) {
        super(message);
    }

    public MapperPhotogramException(String message, Throwable cause) {
        super(message, cause);
    }

}
