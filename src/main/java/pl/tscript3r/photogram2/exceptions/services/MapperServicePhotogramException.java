package pl.tscript3r.photogram2.exceptions.services;

import pl.tscript3r.photogram2.exceptions.mappers.MapperPhotogramException;

public class MapperServicePhotogramException extends MapperPhotogramException {

    public MapperServicePhotogramException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperServicePhotogramException(String message) {
        super(message);
    }
}
