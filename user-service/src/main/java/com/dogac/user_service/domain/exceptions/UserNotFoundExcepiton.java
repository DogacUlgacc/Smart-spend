package com.dogac.user_service.domain.exceptions;

public class UserNotFoundExcepiton extends RuntimeException {
    public UserNotFoundExcepiton(String message) {
        super("User not found!");
    }

}
