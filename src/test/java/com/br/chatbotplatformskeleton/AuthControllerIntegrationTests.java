package com.br.chatbotplatformskeleton;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldLoginUsingEmail() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "admin@chatbot.local",
                      "password": "admin123"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.tokenType", is("Bearer")));
    }

    @Test
    void shouldReturnValidationErrorsForInvalidRegisterPayload() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "",
                      "email": "invalido",
                      "password": "123"
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Dados de entrada invalidos")))
            .andExpect(jsonPath("$.validationErrors", hasSize(3)));
    }

    @Test
    void shouldRejectDuplicateEmailRegistration() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "novo-usuario",
                      "email": "admin@chatbot.local",
                      "password": "segura123"
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Email ja cadastrado")));
    }
}
