package com.accenture.academico.bankingsystem.exceptions;

public class NotAuthorizeException extends RuntimeException{
    public NotAuthorizeException(String message) {
        super(message);
    }
}