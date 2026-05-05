package com.br.chatbotplatformskeleton.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for default pagination behavior.
 * Ensures that Pageable parameters always have valid sort fields.
 */
@Configuration
public class PageableConfiguration implements WebMvcConfigurer {

    /**
     * Creates a default pageable with descending creation date sorting.
     * This is used when clients don't specify sort parameters.
     */
    public static Pageable getDefaultPageable() {
        return PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}

