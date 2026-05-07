package com.br.chatbotplatformskeleton.application.bot.usecase;

public record BotView(
    Long id,
    String name,
    String key,
    Boolean enabled,
    String config
) {
}
