package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.ApiErrorResponse;
import com.br.chatbotplatformskeleton.dto.ApiValidationError;
import com.br.chatbotplatformskeleton.util.InputSanitizer;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.http.ResponseEntity.*;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        @Nullable HttpHeaders headers,
        @Nullable HttpStatusCode status,
        @Nullable WebRequest request
    ) {
        Map<String, String> fieldMessages = new LinkedHashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .forEach(fieldError -> {
                String fieldName = fieldError.getField();
                String message = fieldError.getDefaultMessage();

                // Sanitize field name and message
                String sanitizedField = InputSanitizer.encodeHtmlEntities(fieldName);
                String sanitizedMessage = InputSanitizer.encodeHtmlEntities(message);

                fieldMessages.putIfAbsent(sanitizedField, sanitizedMessage);
            });

        List<ApiValidationError> validationErrors = fieldMessages.entrySet()
            .stream()
            .map(entry -> new ApiValidationError(entry.getKey(), entry.getValue()))
            .toList();

        return badRequest().body(buildResponse(
            HttpStatus.BAD_REQUEST,
            "Dados de entrada invalidos",
            request,
            validationErrors
        ));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        @Nullable HttpMessageNotReadableException ex,
        @Nullable HttpHeaders headers,
        @Nullable HttpStatusCode status,
        @Nullable WebRequest request
    ) {
        return badRequest().body(buildResponse(
            HttpStatus.BAD_REQUEST,
            "Corpo da requisicao invalido",
            request,
            List.of()
        ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ignored, HttpServletRequest request) {
        String sanitizedPath = InputSanitizer.encodeHtmlEntities(request.getRequestURI());
        return status(HttpStatus.UNAUTHORIZED).body(buildResponse(
            HttpStatus.UNAUTHORIZED,
            "Credenciais invalidas",
            sanitizedPath,
            List.of()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        // Sanitize error message to prevent information leakage and XSS
        String safeMessage = InputSanitizer.createSafeErrorMessage(ex.getMessage());
        String sanitizedPath = InputSanitizer.encodeHtmlEntities(request.getRequestURI());

        return badRequest().body(buildResponse(
            HttpStatus.BAD_REQUEST,
            safeMessage,
            sanitizedPath,
            List.of()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ignored, HttpServletRequest request) {
        String sanitizedPath = InputSanitizer.encodeHtmlEntities(request.getRequestURI());
        return status(HttpStatus.CONFLICT).body(buildResponse(
            HttpStatus.CONFLICT,
            "Conflito de dados ao processar a requisicao",
            sanitizedPath,
            List.of()
        ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ignored, HttpServletRequest request) {
        String sanitizedPath = InputSanitizer.encodeHtmlEntities(request.getRequestURI());
        return status(HttpStatus.FORBIDDEN).body(buildResponse(
            HttpStatus.FORBIDDEN,
            "Acesso negado",
            sanitizedPath,
            List.of()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        // Check for potential XSS attempts in exception message
        String exceptionMessage = ex.getMessage();
        if (InputSanitizer.containsXssPayload(exceptionMessage)) {
            logger.warn("Potential XSS attempt detected in exception message from request: {}", request.getRequestURI());
        }

        // Log the full error for debugging purposes (not exposed to client)
        logger.error("Unexpected error occurred", ex);

        // Return generic error message to prevent information leakage
        String sanitizedPath = InputSanitizer.encodeHtmlEntities(request.getRequestURI());
        return status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno inesperado",
            sanitizedPath,
            List.of()
        ));
    }

    private ApiErrorResponse buildResponse(HttpStatus status, String message, @Nullable WebRequest request, List<ApiValidationError> validationErrors) {
        String path = request != null ? request.getDescription(false).replace("uri=", "") : "";
        return buildResponse(status, message, path, validationErrors);
    }

    private ApiErrorResponse buildResponse(HttpStatus status, String message, String path, List<ApiValidationError> validationErrors) {
        String sanitizedPath = InputSanitizer.encodeHtmlEntities(path);
        return new ApiErrorResponse(
            OffsetDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            sanitizedPath,
            validationErrors
        );
    }
}
