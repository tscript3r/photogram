package pl.tscript3r.photogram2.exceptions;

public class PhotogramException extends RuntimeException {

    public PhotogramException(String message) {
        super(message);
    }

    public PhotogramException(String message, Throwable cause) {
        super(message, cause);
    }

}
