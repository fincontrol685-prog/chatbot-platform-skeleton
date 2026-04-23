package com.br.chatbotplatformskeleton.controller;

import com.br.chatbotplatformskeleton.dto.ApiErrorResponse;
import com.br.chatbotplatformskeleton.dto.ApiValidationError;
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

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        Map<String, String> fieldMessages = new LinkedHashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .forEach(fieldError -> fieldMessages.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage()));

        List<ApiValidationError> validationErrors = fieldMessages.entrySet()
            .stream()
            .map(entry -> new ApiValidationError(entry.getKey(), entry.getValue()))
            .toList();

        return ResponseEntity.badRequest().body(buildResponse(
            HttpStatus.BAD_REQUEST,
            "Dados de entrada invalidos",
            request,
            validationErrors
        ));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        return ResponseEntity.badRequest().body(buildResponse(
            HttpStatus.BAD_REQUEST,
            "Corpo da requisicao invalido",
            request,
            List.of()
        ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildResponse(
            HttpStatus.UNAUTHORIZED,
            "Credenciais invalidas",
            request.getRequestURI(),
            List.of()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(buildResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request.getRequestURI(),
            List.of()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildResponse(
            HttpStatus.CONFLICT,
            "Conflito de dados ao processar a requisicao",
            request.getRequestURI(),
            List.of()
        ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildResponse(
            HttpStatus.FORBIDDEN,
            "Acesso negado",
            request.getRequestURI(),
            List.of()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno inesperado",
            request.getRequestURI(),
            List.of()
        ));
    }

    private ApiErrorResponse buildResponse(HttpStatus status, String message, WebRequest request, List<ApiValidationError> validationErrors) {
        String path = request.getDescription(false).replace("uri=", "");
        return buildResponse(status, message, path, validationErrors);
    }

    private ApiErrorResponse buildResponse(HttpStatus status, String message, String path, List<ApiValidationError> validationErrors) {
        return new ApiErrorResponse(
            OffsetDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            path,
            validationErrors
        );
    }
}
