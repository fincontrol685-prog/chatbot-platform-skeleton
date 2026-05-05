package com.br.chatbotplatformskeleton.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for applying rate limiting to controller methods.
 * Used with RateLimitingAspect to enforce request rate limits.
 *
 * Example:
 * @RateLimited(requestsPerMinute = 10, keyByUser = true)
 * @PostMapping("/api/messages")
 * public ResponseEntity<?> sendMessage() { ... }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {

    /**
     * Number of requests allowed per minute
     */
    int requestsPerMinute() default 60;

    /**
     * Whether to use user ID as rate limit key (true) or IP address (false)
     */
    boolean keyByUser() default true;

    /**
     * Name of the rate limit bucket (for monitoring)
     */
    String bucketName() default "";
}

