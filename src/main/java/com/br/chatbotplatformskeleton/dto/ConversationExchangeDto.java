package com.br.chatbotplatformskeleton.dto;

public class ConversationExchangeDto {
    private ConversationMessageDto userMessage;
    private ConversationMessageDto botMessage;

    public ConversationMessageDto getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(ConversationMessageDto userMessage) {
        this.userMessage = userMessage;
    }

    public ConversationMessageDto getBotMessage() {
        return botMessage;
    }

    public void setBotMessage(ConversationMessageDto botMessage) {
        this.botMessage = botMessage;
    }
}
