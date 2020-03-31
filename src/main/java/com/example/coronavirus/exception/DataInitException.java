package com.example.coronavirus.exception;

/**
 * @author Oleg Pavlyukov
 * on 31.03.2020
 * cpabox777@gmail.com
 */
public class DataInitException extends RuntimeException {

    public DataInitException() {
        super();
    }

    public DataInitException(String message) {
        super(message);
    }

    public DataInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
