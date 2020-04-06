package com.example.coronavirus.foreignDataSource.exception;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
public class ResourceNotAvailableException extends Exception {
    public ResourceNotAvailableException() {
        super();
    }

    public ResourceNotAvailableException(String message) {
        super(message);
    }

    public ResourceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotAvailableException(Throwable cause) {
        super(cause);
    }
}
