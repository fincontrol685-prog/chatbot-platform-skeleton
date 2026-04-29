package com.br.chatbotplatformskeleton.util;

import java.util.regex.Pattern;

/**
 * Utility class for sanitizing user inputs and preventing XSS vulnerabilities.
 * Provides methods to encode HTML entities and validate input patterns.
 */
public class InputSanitizer {

    // HTML entity encoding patterns
    private static final char[] DANGEROUS_CHARS = {
        '<', '>', '"', '\'', '&', '%'
    };

    private static final String[] ENTITY_REPLACEMENTS = {
        "&lt;", "&gt;", "&quot;", "&#x27;", "&amp;", "&#x25;"
    };

    // Pattern to detect potential XSS attempts
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(?i)<(script|iframe|object|embed|link|meta)\\b|" +
        "(?i)on\\w+\\s*=|" +
        "(?i)javascript:|" +
        "(?i)vbscript:|" +
        "(?i)data:text/html"
    );

    /**
     * Encodes HTML special characters to prevent HTML injection and XSS attacks.
     * Converts dangerous characters to their HTML entity equivalents.
     *
     * @param input The string to encode
     * @return HTML-encoded string safe for use in HTML context
     */
    public static String encodeHtmlEntities(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            int idx = indexOfChar(c);
            if (idx >= 0) {
                result.append(ENTITY_REPLACEMENTS[idx]);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Finds the index of a character in the dangerous characters list.
     *
     * @param c The character to find
     * @return The index if found, -1 otherwise
     */
    private static int indexOfChar(char c) {
        for (int i = 0; i < DANGEROUS_CHARS.length; i++) {
            if (DANGEROUS_CHARS[i] == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Detects potential XSS payloads in the input string.
     * Uses pattern matching to identify common XSS attack vectors.
     *
     * @param input The string to check
     * @return true if potential XSS payload is detected, false otherwise
     */
    public static boolean containsXssPayload(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        return XSS_PATTERN.matcher(input).find();
    }

    /**
     * Removes potentially dangerous HTML/JavaScript from input.
     * Strips script tags, event handlers, and other dangerous constructs.
     *
     * @param input The string to clean
     * @return Cleaned string with dangerous content removed
     */
    public static String stripXssPayload(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Remove script tags and their content
        String cleaned = input.replaceAll("(?i)<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>", "");

        // Remove event handlers (onclick, onload, onerror, etc.)
        cleaned = cleaned.replaceAll("(?i)\\son\\w+\\s*=\\s*[\"']?(?:[^\"'\\s>]|(?<=\\\\))*[\"']?", "");

        // Remove dangerous protocols
        cleaned = cleaned.replaceAll("(?i)(?:javascript|vbscript|data:text\\/html):", "");

        // Remove iframe, object, embed, link, meta tags
        cleaned = cleaned.replaceAll("(?i)<(iframe|object|embed|link|meta)\\b[^>]*>", "");

        return cleaned.trim();
    }

    /**
     * Validates that a string is safe for use in JSON context.
     * This is generally safe as JSON parsers handle escaping, but we check for control characters.
     *
     * @param input The string to validate
     * @return true if the string is safe for JSON context, false otherwise
     */
    public static boolean isSafeForJson(String input) {
        if (input == null) {
            return true;
        }

        // Check for unescaped control characters
        for (char c : input.toCharArray()) {
            if (c < 0x20 && c != '\t' && c != '\n' && c != '\r') {
                return false;
            }
        }

        return true;
    }

    /**
     * Normalizes user input by trimming and limiting length.
     * Prevents potential DoS attacks through excessively long inputs.
     *
     * @param input The string to normalize
     * @param maxLength Maximum allowed length (-1 for unlimited)
     * @return Normalized string, or null if input is null
     */
    public static String normalizeInput(String input, int maxLength) {
        if (input == null) {
            return null;
        }

        String trimmed = input.trim();

        if (maxLength > 0 && trimmed.length() > maxLength) {
            return trimmed.substring(0, maxLength);
        }

        return trimmed;
    }

    /**
     * Creates a safe error message by encoding HTML entities.
     * Use this when displaying user-controlled data in error messages.
     *
     * @param message The error message that may contain user input
     * @return Safe error message with HTML entities encoded
     */
    public static String createSafeErrorMessage(String message) {
        if (message == null || message.isEmpty()) {
            return "Um erro desconhecido ocorreu";
        }

        String sanitized = encodeHtmlEntities(message);
        // Limit error message length to prevent information leakage
        if (sanitized.length() > 500) {
            sanitized = sanitized.substring(0, 500);
        }

        return sanitized;
    }

    /**
     * Validates that a URL is safe (not a javascript: or data: URI).
     *
     * @param url The URL to validate
     * @return true if the URL is safe, false otherwise
     */
    public static boolean isSafeUrl(String url) {
        if (url == null || url.isEmpty()) {
            return true;
        }

        String lowerUrl = url.toLowerCase().trim();

        // Reject dangerous protocols
        return !lowerUrl.startsWith("javascript:") &&
               !lowerUrl.startsWith("data:") &&
               !lowerUrl.startsWith("vbscript:");
    }
}

