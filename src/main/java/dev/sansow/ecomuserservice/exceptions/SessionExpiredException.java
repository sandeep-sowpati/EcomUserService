package dev.sansow.ecomuserservice.exceptions;

public class SessionExpiredException extends  Exception{
    public SessionExpiredException() {
    }

    public SessionExpiredException(String message) {
        super(message);
    }

    public SessionExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionExpiredException(Throwable cause) {
        super(cause);
    }

    public SessionExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
