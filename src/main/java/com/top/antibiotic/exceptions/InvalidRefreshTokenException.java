package com.top.antibiotic.exceptions;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(String exMessage) {
        super(exMessage);
    }
}
