package com.br.chatbotplatformskeleton.application.bot.usecase;

public record UpsertBotCommand(
    String name,
    String key,
    Boolean enabled,
    String config
) {
}
