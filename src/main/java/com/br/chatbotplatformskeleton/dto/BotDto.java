package com.br.chatbotplatformskeleton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BotDto {
    private Long id;

    @NotBlank(message = "Nome do bot e obrigatorio")
    @Size(max = 120, message = "Nome do bot deve ter no maximo 120 caracteres")
    private String name;

    @NotBlank(message = "Chave do bot e obrigatoria")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Chave do bot deve usar apenas letras, numeros, _ ou -")
    @Size(max = 80, message = "Chave do bot deve ter no maximo 80 caracteres")
    private String key;

    private Boolean enabled;

    @Size(max = 10000, message = "Configuracao excede o tamanho maximo permitido")
    private String config;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }
}
