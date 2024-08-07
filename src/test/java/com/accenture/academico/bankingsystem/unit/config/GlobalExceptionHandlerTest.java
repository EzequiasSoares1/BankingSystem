package com.accenture.academico.bankingsystem.unit.config;
import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.config.GlobalExceptionHandler;
import com.accenture.academico.bankingsystem.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest implements ConfigSpringTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleNoHandlerFoundException() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/example", null);
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleNoHandlerFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No endpoint GET /example", responseEntity.getBody());
    }

    @Test
    void handleMethodNotSupported() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleMethodNotSupported(ex);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        assertEquals("Request method 'POST' is not supported for this endpoint", responseEntity.getBody());
    }

    @Test
    void handleRuntimeException() {
        RuntimeException ex = new RuntimeException("Internal server error");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleRuntimeException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal server error", responseEntity.getBody());
    }

    @Test
    void handleGeneralSecurityException() {
        GeneralSecurityException ex = new GeneralSecurityException("Security issue");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleGeneralSecurityException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Problem connecting to Google Drive:", responseEntity.getBody());
    }

    @Test
    void handleIOException() {
        IOException ex = new IOException("IO error");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleIOException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void handleDatabaseException_SQLTransientConnectionException() {
        SQLTransientConnectionException ex = new SQLTransientConnectionException("Connection error");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleDatabaseException(ex);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
        assertEquals("Database connection error. Please try again later.", responseEntity.getBody());
    }

    @Test
    void handleDatabaseException_DataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Integrity violation");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleDatabaseException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Database integrity violation. Please check your data.", responseEntity.getBody());
    }

    @Test
    void handleDatabaseException_ConstraintViolationException() {
        ConstraintViolationException ex = new ConstraintViolationException("Constraint violation", null);
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleDatabaseException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Database error. Please try again later.", responseEntity.getBody());
    }

    @Test
    void handleDatabaseException_SQLException() {
        SQLException ex = new SQLException("SQL error");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleDatabaseException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error: SQL error", responseEntity.getBody());
    }

    @Test
    void handleDatabaseException_CannotCreateTransactionException() {
        CannotCreateTransactionException ex = new CannotCreateTransactionException("Transaction error", new SQLException());
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleDatabaseException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Database error. Please try again later.", responseEntity.getBody());
    }

    @Test
    void handleNotFoundException() {
        NotFoundException ex = new NotFoundException("Resource not found");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Resource not found", responseEntity.getBody());
    }

    @Test
    void handleInternalLogicException() {
        InternalLogicException ex = new InternalLogicException("Logic error");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleInternalLogicException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Logic error", responseEntity.getBody());
    }

    @Test
    void handleConflictException() {
        ConflictException ex = new ConflictException("Conflict error");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleConflictException(ex);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Conflict error", responseEntity.getBody());
    }

    @Test
    void handleNotAuthorizeException() {
        NotAuthorizeException ex = new NotAuthorizeException("Authorization error");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleNotAuthorizeException(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Authorization error", responseEntity.getBody());
    }

    @Test
    void handleInternalErrorException() {
        InternalErrorException ex = new InternalErrorException("Internal error");
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleInternalErrorException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
}
