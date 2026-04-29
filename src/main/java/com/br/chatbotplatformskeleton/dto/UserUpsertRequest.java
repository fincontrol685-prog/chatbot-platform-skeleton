package com.br.chatbotplatformskeleton.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

public class UserUpsertRequest {
    @NotBlank(message = "Usuario e obrigatorio")
    @Size(max = 80, message = "Usuario deve ter no maximo 80 caracteres")
    private String username;

    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email invalido")
    @Size(max = 160, message = "Email deve ter no maximo 160 caracteres")
    private String email;

    @Size(max = 120, message = "Senha deve ter no maximo 120 caracteres")
    private String password;

    private Boolean enabled = true;

    private Set<String> roles = new LinkedHashSet<>();

    private Set<Long> departmentIds = new LinkedHashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(Set<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }
}
