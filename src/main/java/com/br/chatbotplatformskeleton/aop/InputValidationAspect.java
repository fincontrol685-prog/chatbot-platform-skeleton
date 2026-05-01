package com.br.chatbotplatformskeleton.aop;

import com.br.chatbotplatformskeleton.dto.*;
import com.br.chatbotplatformskeleton.util.InputSanitizer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * AOP Aspect for validating and sanitizing input data in controller methods.
 * Intercepts @PostMapping and @PutMapping methods to validate input.
 */
@Aspect
@Component
public class InputValidationAspect {

    private static final Logger log = LoggerFactory.getLogger(InputValidationAspect.class);
    private final InputSanitizer inputSanitizer;

    public InputValidationAspect(InputSanitizer inputSanitizer) {
        this.inputSanitizer = inputSanitizer;
    }

    /**
     * Intercepts all PostMapping and PutMapping methods in controllers
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public Object validateInput(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Input validation started for method: {}", joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BotDto) {
                sanitizeBotDto((BotDto) arg);
            } else if (arg instanceof ConversationDto) {
                sanitizeConversationDto((ConversationDto) arg);
            } else if (arg instanceof ConversationMessageDto) {
                sanitizeConversationMessageDto((ConversationMessageDto) arg);
            } else if (arg instanceof String) {
                // Validate string arguments
                log.debug("String argument passed to endpoint: {}", arg);
            }
        }

        return joinPoint.proceed();
    }

    /**
     * Sanitizes BotDto fields
     */
    private void sanitizeBotDto(BotDto dto) {
        if (dto.getName() != null) {
            inputSanitizer.validateNotEmpty(dto.getName(), "Bot name");
            dto.setName(inputSanitizer.sanitize(dto.getName()));
        }

        if (dto.getKey() != null) {
            inputSanitizer.validateNotEmpty(dto.getKey(), "Bot key");
            dto.setKey(inputSanitizer.sanitize(dto.getKey()));

            // Validate bot key format (alphanumeric and hyphens only)
            if (!dto.getKey().matches("^[a-zA-Z0-9-]+$")) {
                throw new IllegalArgumentException("Bot key can only contain letters, numbers, and hyphens");
            }
        }

        if (dto.getConfig() != null) {
            dto.setConfig(inputSanitizer.sanitize(dto.getConfig()));
        }

        log.debug("BotDto sanitized successfully");
    }

    /**
     * Sanitizes ConversationDto fields
     */
    private void sanitizeConversationDto(ConversationDto dto) {
        if (dto.getTitle() != null) {
            inputSanitizer.validateNotEmpty(dto.getTitle(), "Conversation title");
            dto.setTitle(inputSanitizer.sanitize(dto.getTitle()));
        }

        // Validate status if provided
        if (dto.getStatus() != null) {
            String status = dto.getStatus().toUpperCase();
            if (!status.matches("^(ACTIVE|CLOSED|ARCHIVED|DRAFT)$")) {
                throw new IllegalArgumentException("Invalid conversation status: " + status);
            }
        }

        if (dto.getMetadata() != null) {
            dto.setMetadata(inputSanitizer.sanitize(dto.getMetadata()));
        }

        log.debug("ConversationDto sanitized successfully");
    }

    /**
     * Sanitizes ConversationMessageDto fields
     */
    private void sanitizeConversationMessageDto(ConversationMessageDto dto) {
        if (dto.getContent() != null) {
            inputSanitizer.validateNotEmpty(dto.getContent(), "Message content");
            dto.setContent(inputSanitizer.sanitize(dto.getContent()));
        }

        if (dto.getMessageType() != null) {
            String msgType = dto.getMessageType().toUpperCase();
            if (!msgType.matches("^(TEXT|IMAGE|AUDIO|VIDEO|FILE)$")) {
                throw new IllegalArgumentException("Invalid message type: " + msgType);
            }
        }

        log.debug("ConversationMessageDto sanitized successfully");
    }

    /**
     * Generic method to sanitize any object by reflection
     */
    public void sanitizeObject(Object obj) {
        if (obj == null) {
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (!field.getType().equals(String.class)) {
                continue;
            }

            field.setAccessible(true);
            try {
                String value = (String) field.get(obj);
                if (value != null) {
                    field.set(obj, inputSanitizer.sanitize(value));
                }
            } catch (IllegalAccessException e) {
                log.warn("Failed to sanitize field: {}", field.getName(), e);
            } finally {
                field.setAccessible(false);
            }
        }
    }
}

