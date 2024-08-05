package com.accenture.academico.bankingsystem.exceptions;

public class InternalErrorException  extends RuntimeException{
    public InternalErrorException(String message) {
        super(message);
    }
}
