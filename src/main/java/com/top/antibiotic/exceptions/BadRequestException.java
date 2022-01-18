package com.top.antibiotic.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String exMessage) {
        super(exMessage);
    }
}
