package com.br.chatbotplatformskeleton.application.bot.service;

import com.br.chatbotplatformskeleton.application.bot.model.BotModel;
import com.br.chatbotplatformskeleton.application.bot.usecase.BotView;
import com.br.chatbotplatformskeleton.application.bot.usecase.UpsertBotCommand;
import com.br.chatbotplatformskeleton.application.port.out.BotConfigFormatter;
import com.br.chatbotplatformskeleton.application.port.out.BotPersistencePort;
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
class BotManagementServiceTest {

    @Mock
    private BotPersistencePort botPersistencePort;

    @Mock
    private BotConfigFormatter botConfigFormatter;

    private BotManagementService botManagementService;

    @BeforeEach
    void setUp() {
        botManagementService = new BotManagementService(botPersistencePort, botConfigFormatter);
    }

    @Test
    void createShouldFormatConfigBeforePersisting() {
        UpsertBotCommand command = new UpsertBotCommand(
            " Atendimento Prime ",
            " atendimento-prime ",
            true,
            "{\"profile\":{\"assistantRole\":\"Analista virtual\"}}"
        );

        when(botPersistencePort.existsByKeyIgnoreCase("atendimento-prime")).thenReturn(false);
        when(botConfigFormatter.normalize(command.config()))
            .thenReturn("{\n  \"profile\" : {\n    \"assistantRole\" : \"Analista virtual\"\n  }\n}");
        when(botPersistencePort.save(any(BotModel.class))).thenAnswer(invocation -> {
            BotModel bot = invocation.getArgument(0);
            bot.setId(10L);
            return bot;
        });

        BotView created = botManagementService.create(command);

        ArgumentCaptor<BotModel> captor = ArgumentCaptor.forClass(BotModel.class);
        verify(botPersistencePort).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("Atendimento Prime");
        assertThat(captor.getValue().getKey()).isEqualTo("atendimento-prime");
        assertThat(captor.getValue().getConfig()).contains("\n");
        assertThat(created.config()).contains("\"assistantRole\" : \"Analista virtual\"");
    }

    @Test
    void createShouldKeepLegacyTextConfigWhenPayloadIsNotJson() {
        UpsertBotCommand command = new UpsertBotCommand(
            "Bot legado",
            "bot-legado",
            true,
            "Observacao interna para rollout controlado"
        );

        when(botPersistencePort.existsByKeyIgnoreCase("bot-legado")).thenReturn(false);
        when(botConfigFormatter.normalize(command.config())).thenReturn("Observacao interna para rollout controlado");
        when(botPersistencePort.save(any(BotModel.class))).thenAnswer(invocation -> {
            BotModel bot = invocation.getArgument(0);
            bot.setId(11L);
            return bot;
        });

        BotView created = botManagementService.create(command);

        assertThat(created.config()).isEqualTo("Observacao interna para rollout controlado");
    }

    @Test
    void updateShouldRejectDuplicatedKeyFromAnotherBot() {
        BotModel existing = new BotModel();
        existing.setId(7L);
        existing.setName("Bot atual");
        existing.setKey("bot-atual");
        existing.setEnabled(true);

        UpsertBotCommand command = new UpsertBotCommand("Bot atual", "bot-duplicado", true, null);

        when(botPersistencePort.findById(7L)).thenReturn(Optional.of(existing));
        when(botPersistencePort.existsByKeyIgnoreCaseAndIdNot("bot-duplicado", 7L)).thenReturn(true);

        assertThatThrownBy(() -> botManagementService.update(7L, command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Ja existe um bot com esta chave");
    }

    @Test
    void listAllShouldReturnApplicationView() {
        BotModel bot = new BotModel();
        bot.setId(3L);
        bot.setName("Bot de suporte");
        bot.setKey("bot-suporte");
        bot.setEnabled(true);
        bot.setConfig("{\"channel\":\"web\"}");

        when(botPersistencePort.findAll()).thenReturn(List.of(bot));

        List<BotView> result = botManagementService.listAll();

        assertThat(result)
            .singleElement()
            .satisfies(view -> {
                assertThat(view.id()).isEqualTo(3L);
                assertThat(view.name()).isEqualTo("Bot de suporte");
                assertThat(view.key()).isEqualTo("bot-suporte");
                assertThat(view.enabled()).isTrue();
                assertThat(view.config()).isEqualTo("{\"channel\":\"web\"}");
            });
    }
}
