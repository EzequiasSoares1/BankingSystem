package com.accenture.academico.bankingsystem.exception;

public class NotAuthorizeException extends RuntimeException{
    public NotAuthorizeException(String message) {
        super(message);
    }
}