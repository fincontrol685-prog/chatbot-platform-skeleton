package com.br.chatbotplatformskeleton.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "BOT")
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "bot_key", unique = true)
    private String key;

    private Boolean enabled = true;

    @Lob
    private String config;

    // getters and setters
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
