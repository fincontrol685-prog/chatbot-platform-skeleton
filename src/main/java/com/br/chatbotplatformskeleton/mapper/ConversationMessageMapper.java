package com.br.chatbotplatformskeleton.mapper;

import com.br.chatbotplatformskeleton.domain.ConversationMessage;
import com.br.chatbotplatformskeleton.dto.ConversationMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for ConversationMessage entity to ConversationMessageDto conversions.
 * Centralizes all ConversationMessage ↔ ConversationMessageDto transformations.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConversationMessageMapper {

    /**
     * Converts a ConversationMessage entity to a ConversationMessageDto.
     *
     * @param message the ConversationMessage entity to convert
     * @return the corresponding ConversationMessageDto
     */
    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(source = "sender.id", target = "senderId")
    ConversationMessageDto toDto(ConversationMessage message);

    /**
     * Converts a ConversationMessageDto to a ConversationMessage entity.
     * Note: Relationships and timestamps are managed by JPA.
     *
     * @param dto the ConversationMessageDto to convert
     * @return the corresponding ConversationMessage entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ConversationMessage toEntity(ConversationMessageDto dto);
}

