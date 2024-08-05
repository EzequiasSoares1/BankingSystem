package com.accenture.academico.bankingsystem.exceptions;

public class ConflictException extends RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}
