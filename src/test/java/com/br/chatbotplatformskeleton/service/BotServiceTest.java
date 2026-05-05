package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.dto.BotDto;
import com.br.chatbotplatformskeleton.mapper.BotMapper;
import com.br.chatbotplatformskeleton.repository.BotRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotServiceTest {

    @Mock
    private BotRepository botRepository;

    @Mock
    private BotMapper botMapper;

    private BotService botService;

    @BeforeEach
    void setUp() {
        botService = new BotService(botRepository, new ObjectMapper(), botMapper);
    }

    @Test
    void createShouldFormatJsonConfigBeforePersisting() {
        BotDto dto = new BotDto();
        dto.setName(" Atendimento Prime ");
        dto.setKey(" atendimento-prime ");
        dto.setEnabled(true);
        dto.setConfig("{\"profile\":{\"assistantRole\":\"Analista virtual\"}}");

        when(botRepository.existsByKeyIgnoreCase("atendimento-prime")).thenReturn(false);
        when(botRepository.save(any(Bot.class))).thenAnswer(invocation -> {
            Bot bot = invocation.getArgument(0);
            bot.setId(10L);
            return bot;
        });

        BotDto expectedResult = new BotDto();
        expectedResult.setId(10L);
        expectedResult.setName("Atendimento Prime");
        expectedResult.setKey("atendimento-prime");
        expectedResult.setEnabled(true);
        expectedResult.setConfig("{\"profile\" : {\"assistantRole\" : \"Analista virtual\"}}");

        when(botMapper.toDto(any(Bot.class))).thenReturn(expectedResult);

        BotDto created = botService.create(dto);

        ArgumentCaptor<Bot> captor = ArgumentCaptor.forClass(Bot.class);
        verify(botRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("Atendimento Prime");
        assertThat(captor.getValue().getKey()).isEqualTo("atendimento-prime");
        assertThat(captor.getValue().getConfig()).contains("\n");
        assertThat(created.getConfig()).contains("\"assistantRole\" : \"Analista virtual\"");
    }

    @Test
    void createShouldKeepLegacyTextConfigWhenPayloadIsNotJson() {
        BotDto dto = new BotDto();
        dto.setName("Bot legado");
        dto.setKey("bot-legado");
        dto.setConfig("Observacao interna para rollout controlado");

        when(botRepository.existsByKeyIgnoreCase("bot-legado")).thenReturn(false);
        when(botRepository.save(any(Bot.class))).thenAnswer(invocation -> {
            Bot bot = invocation.getArgument(0);
            bot.setId(11L);
            return bot;
        });

        BotDto expectedResult = new BotDto();
        expectedResult.setId(11L);
        expectedResult.setName("Bot legado");
        expectedResult.setKey("bot-legado");
        expectedResult.setConfig("Observacao interna para rollout controlado");

        when(botMapper.toDto(any(Bot.class))).thenReturn(expectedResult);

        BotDto created = botService.create(dto);

        assertThat(created.getConfig()).isEqualTo("Observacao interna para rollout controlado");
    }

    @Test
    void updateShouldRejectDuplicatedKeyFromAnotherBot() {
        Bot existing = new Bot();
        existing.setId(7L);
        existing.setName("Bot atual");
        existing.setKey("bot-atual");
        existing.setEnabled(true);

        BotDto dto = new BotDto();
        dto.setName("Bot atual");
        dto.setKey("bot-duplicado");
        dto.setEnabled(true);

        when(botRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(botRepository.existsByKeyIgnoreCaseAndIdNot("bot-duplicado", 7L)).thenReturn(true);

        assertThatThrownBy(() -> botService.update(7L, dto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Ja existe um bot com esta chave");
    }

    @Test
    void listAllShouldFallbackToGeneratedMapperWhenInjectedMapperIsNull() {
        BotService serviceWithFallbackMapper = new BotService(botRepository, new ObjectMapper(), null);

        Bot bot = new Bot();
        bot.setId(3L);
        bot.setName("Bot de suporte");
        bot.setKey("bot-suporte");
        bot.setEnabled(true);
        bot.setConfig("{\"channel\":\"web\"}");

        when(botRepository.findAll()).thenReturn(List.of(bot));

        List<BotDto> result = serviceWithFallbackMapper.listAll();

        assertThat(result)
            .singleElement()
            .satisfies(dto -> {
                assertThat(dto.getId()).isEqualTo(3L);
                assertThat(dto.getName()).isEqualTo("Bot de suporte");
                assertThat(dto.getKey()).isEqualTo("bot-suporte");
                assertThat(dto.getEnabled()).isTrue();
                assertThat(dto.getConfig()).isEqualTo("{\"channel\":\"web\"}");
            });
    }
}
