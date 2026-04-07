package com.br.chatbotplatformskeleton;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for Security Configuration
 * Tests security headers, rate limiting, and authentication
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigurationTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test that security headers are present in responses
     */
    @Test
    public void testSecurityHeadersPresent() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(header().exists("X-Frame-Options"))
            .andExpect(header().exists("X-Content-Type-Options"))
            .andExpect(header().exists("Content-Security-Policy"))
            .andExpect(header().string("X-Frame-Options", "DENY"));
    }

    /**
     * Test that health check is publicly accessible
     */
    @Test
    public void testHealthCheckPublicAccess() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk());
    }

    /**
     * Test that unauthorized requests are rejected for protected endpoints
     */
    @Test
    public void testUnauthorizedAccessDenied() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
            .andExpect(status().isForbidden());
    }

    /**
     * Test that metrics endpoint requires authentication
     */
    @Test
    public void testMetricsRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/actuator/metrics"))
            .andExpect(status().isForbidden());
    }

    /**
     * Test CORS preflight requests
     */
    @Test
    public void testCORSPreflight() throws Exception {
        mockMvc.perform(options("/api/auth/login")
            .header("Origin", "https://chatbot.company.com")
            .header("Access-Control-Request-Method", "POST")
            .header("Access-Control-Request-Headers", "Content-Type"))
            .andExpect(status().isForbidden());
    }

    /**
     * Test cache control headers for API endpoints
     */
    @Test
    public void testCacheControlHeaders() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(header().exists("Cache-Control"));
    }

    /**
     * Test CSP header configuration
     */
    @Test
    public void testContentSecurityPolicyHeader() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(header().string("Content-Security-Policy",
                containsString("default-src")));
    }
}

