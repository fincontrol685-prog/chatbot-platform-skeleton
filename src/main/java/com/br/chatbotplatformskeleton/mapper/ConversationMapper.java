package com.br.chatbotplatformskeleton.mapper;

import com.br.chatbotplatformskeleton.domain.Conversation;
import com.br.chatbotplatformskeleton.dto.ConversationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for Conversation entity to ConversationDto conversions.
 * Centralizes all Conversation ↔ ConversationDto transformations to eliminate duplication across services.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConversationMapper {
    
    /**
     * Converts a Conversation entity to a ConversationDto.
     * Extracts bot and user IDs from relationships.
     *
     * @param conversation the Conversation entity to convert
     * @return the corresponding ConversationDto
     */
    @Mapping(source = "bot.id", target = "botId")
    @Mapping(source = "user.id", target = "userId")
    ConversationDto toDto(Conversation conversation);
    
    /**
     * Converts a ConversationDto to a Conversation entity.
     * Note: Bot and User entities as well as timestamps are typically managed by JPA.
     *
     * @param dto the ConversationDto to convert
     * @return the corresponding Conversation entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bot", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Conversation toEntity(ConversationDto dto);
}

