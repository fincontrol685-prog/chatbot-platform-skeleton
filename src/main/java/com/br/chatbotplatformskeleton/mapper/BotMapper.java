package com.br.chatbotplatformskeleton.mapper;

import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.dto.BotDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for Bot entity to BotDto conversions.
 * Centralizes all Bot ↔ BotDto transformations to eliminate duplication across services.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BotMapper {
    
    /**
     * Converts a Bot entity to a BotDto.
     *
     * @param bot the Bot entity to convert
     * @return the corresponding BotDto
     */
    BotDto toDto(Bot bot);
    
    /**
     * Converts a BotDto to a Bot entity.
     * Note: ID is typically managed by JPA.
     *
     * @param dto the BotDto to convert
     * @return the corresponding Bot entity
     */
    @Mapping(target = "id", ignore = true)
    Bot toEntity(BotDto dto);
}

