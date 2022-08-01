package com.top.antibiotic.exceptions;

import lombok.Getter;

@Getter
public class ImportExcelRowException extends RuntimeException {
    public ImportExcelRowException(String exMessage, String receivedValue, String expectedType, Integer cellNumber) {
        super(exMessage);
        this.receivedValue = receivedValue;
        this.expectedType = expectedType;
        this.cellNumber = cellNumber;
    }

    public ImportExcelRowException(String exMessage, Throwable cause,
                                   String receivedValue, String expectedType, Integer cellNumber) {
        super(exMessage, cause);
        this.receivedValue = receivedValue;
        this.expectedType = expectedType;
        this.cellNumber = cellNumber;
    }

    private final String receivedValue;
    private final String expectedType;
    private final Integer cellNumber;
}
