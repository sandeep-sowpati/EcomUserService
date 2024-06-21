package dev.sansow.ecomuserservice.exceptions;

public class InvalidRolesProvidedException extends  Exception{
    public InvalidRolesProvidedException() {
    }

    public InvalidRolesProvidedException(String message) {
        super(message);
    }

    public InvalidRolesProvidedException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRolesProvidedException(Throwable cause) {
        super(cause);
    }

    public InvalidRolesProvidedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
