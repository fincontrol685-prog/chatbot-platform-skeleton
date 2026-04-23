package com.br.chatbotplatformskeleton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    @NotBlank(message = "Token e obrigatorio")
    private String token;

    @NotBlank(message = "Nova senha e obrigatoria")
    @Size(min = 8, max = 72, message = "Nova senha deve ter entre 8 e 72 caracteres")
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
