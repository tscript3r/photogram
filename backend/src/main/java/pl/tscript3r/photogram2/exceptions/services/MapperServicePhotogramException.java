package pl.tscript3r.photogram2.exceptions.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.tscript3r.photogram2.exceptions.mappers.MapperPhotogramException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class MapperServicePhotogramException extends MapperPhotogramException {

    public MapperServicePhotogramException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperServicePhotogramException(String message) {
        super(message);
    }

}
