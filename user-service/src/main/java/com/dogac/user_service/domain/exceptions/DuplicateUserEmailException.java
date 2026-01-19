package com.dogac.user_service.domain.exceptions;

public class DuplicateUserEmailException extends RuntimeException {
    public DuplicateUserEmailException(String message) {
        super(message);
    }

    public DuplicateUserEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
