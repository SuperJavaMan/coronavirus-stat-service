package com.example.coronavirus.exception;

/**
 * @author Oleg Pavlyukov
 * on 31.03.2020
 * cpabox777@gmail.com
 */
public class NoDataException extends Exception {
    public NoDataException() {
        super();
    }

    public NoDataException(String message) {
        super(message);
    }

    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
