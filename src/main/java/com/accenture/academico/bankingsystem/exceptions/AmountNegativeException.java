package com.accenture.academico.bankingsystem.exceptions;

public class AmountNegativeException extends RuntimeException{
    public AmountNegativeException(String message) {
        super(message);
    }
}
