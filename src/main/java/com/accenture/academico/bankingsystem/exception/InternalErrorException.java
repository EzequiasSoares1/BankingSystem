package com.accenture.academico.bankingsystem.exception;

public class InternalErrorException  extends RuntimeException{
    public InternalErrorException(String message) {
        super(message);
    }
}
