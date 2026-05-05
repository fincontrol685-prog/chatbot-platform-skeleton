package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Bot;
import com.br.chatbotplatformskeleton.domain.Conversation;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.ConversationDto;
import com.br.chatbotplatformskeleton.mapper.ConversationMapper;
import com.br.chatbotplatformskeleton.repository.BotRepository;
import com.br.chatbotplatformskeleton.repository.ConversationRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private BotRepository botRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private ConversationMapper conversationMapper;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private ConversationService conversationService;

    private Conversation testConversation;
    private ConversationDto testConversationDto;
    private Bot testBot;
    private UserAccount testUser;

    @BeforeEach
    void setUp() {
        testBot = new Bot();
        testBot.setId(1L);
        testBot.setName("Test Bot");

        testUser = new UserAccount();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testConversation = new Conversation();
        testConversation.setId(1L);
        testConversation.setBot(testBot);
        testConversation.setUser(testUser);
        testConversation.setTitle("Test Conversation");
        testConversation.setStatus("ACTIVE");
        testConversation.setMessageCount(0L);
        testConversation.setCreatedAt(OffsetDateTime.now());

        testConversationDto = new ConversationDto();
        testConversationDto.setId(1L);
        testConversationDto.setBotId(1L);
        testConversationDto.setUserId(1L);
        testConversationDto.setTitle("Test Conversation");
        testConversationDto.setStatus("ACTIVE");
        testConversationDto.setMessageCount(0L);

        lenient().when(currentUserService.isPrivileged(any(UserAccount.class))).thenReturn(false);
    }

    @Test
    void testCreateConversationSuccess() {
        // Arrange
        ConversationDto newConvDto = new ConversationDto();
        newConvDto.setBotId(1L);
        newConvDto.setTitle("New Conversation");
        newConvDto.setMetadata(null);

        Conversation createdConv = new Conversation();
        createdConv.setId(2L);
        createdConv.setBot(testBot);
        createdConv.setUser(testUser);
        createdConv.setTitle("New Conversation");
        createdConv.setStatus("ACTIVE");
        createdConv.setMessageCount(0L);

        ConversationDto dto = new ConversationDto();
        dto.setId(2L);
        dto.setBotId(1L);
        dto.setUserId(1L);
        dto.setTitle("New Conversation");
        dto.setStatus("ACTIVE");
        dto.setMessageCount(0L);

        when(botRepository.findById(1L)).thenReturn(Optional.of(testBot));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(conversationRepository.save(any(Conversation.class))).thenReturn(createdConv);
        when(conversationMapper.toDto(createdConv)).thenReturn(dto);




        when(conversationMapper.toDto(createdConv)).thenReturn(dto);
        // Act
        ConversationDto result = conversationService.create(newConvDto, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("New Conversation", result.getTitle());
        verify(botRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(conversationRepository, times(1)).save(any(Conversation.class));
        verify(auditService, times(1)).log(anyLong(), anyString(), anyString(), anyLong(), any(), any());
    }

    @Test
    void testCreateConversationBotNotFound() {
        // Arrange
        ConversationDto newConvDto = new ConversationDto();
        newConvDto.setBotId(999L);
        newConvDto.setTitle("New Conversation");

        when(botRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> conversationService.create(newConvDto, 1L));
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    void testCreateConversationUserNotFound() {
        // Arrange
        ConversationDto newConvDto = new ConversationDto();
        newConvDto.setBotId(1L);
        newConvDto.setTitle("New Conversation");

        when(botRepository.findById(1L)).thenReturn(Optional.of(testBot));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> conversationService.create(newConvDto, 999L));
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    void testFindByIdExists() {
        // Arrange
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
        when(conversationMapper.toDto(testConversation)).thenReturn(testConversationDto);

        // Act
        Optional<ConversationDto> result = conversationService.findById(1L, testUser);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Conversation", result.get().getTitle());
        verify(conversationRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotExists() {
        // Arrange
        when(conversationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<ConversationDto> result = conversationService.findById(999L, testUser);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByBotId() {
        // Arrange
        Page<Conversation> conversationPage = new PageImpl<>(List.of(testConversation));
        when(conversationRepository.findByBotId(eq(1L), any())).thenReturn(conversationPage);
        when(conversationMapper.toDto(testConversation)).thenReturn(testConversationDto);

        // Act
        Page<ConversationDto> result = conversationService.findByBotId(1L, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(conversationRepository, times(1)).findByBotId(eq(1L), any());
    }

    @Test
    void testFindByUserId() {
        // Arrange
        Page<Conversation> conversationPage = new PageImpl<>(List.of(testConversation));
        when(conversationRepository.findByUserId(eq(1L), any())).thenReturn(conversationPage);
        when(conversationMapper.toDto(testConversation)).thenReturn(testConversationDto);

        // Act
        Page<ConversationDto> result = conversationService.findByUserId(1L, PageRequest.of(0, 10), testUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testCloseConversationSuccess() {
        // Arrange
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
        when(conversationRepository.save(any(Conversation.class))).thenReturn(testConversation);
        when(conversationMapper.toDto(testConversation)).thenReturn(testConversationDto);

        // Act
        Optional<ConversationDto> result = conversationService.closeConversation(1L, 1L);

        // Assert
        assertTrue(result.isPresent());
        verify(conversationRepository, times(1)).findById(1L);
        verify(conversationRepository, times(1)).save(any(Conversation.class));
        verify(auditService, times(1)).log(anyLong(), anyString(), anyString(), anyLong(), any(), any());
    }

    @Test
    void testCloseConversationNotFound() {
        // Arrange
        when(conversationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<ConversationDto> result = conversationService.closeConversation(999L, 1L);

        // Assert
        assertFalse(result.isPresent());
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    void testFindByIdShouldRejectConversationFromAnotherUser() {
        UserAccount anotherUser = new UserAccount();
        anotherUser.setId(99L);
        anotherUser.setUsername("another-user");

        when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));

        assertThrows(AccessDeniedException.class, () -> conversationService.findById(1L, anotherUser));
        verify(conversationMapper, never()).toDto(any(Conversation.class));
    }

    @Test
    void testUpdateTitleSuccess() {
        // Arrange
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
        when(conversationRepository.save(any(Conversation.class))).thenReturn(testConversation);
        when(conversationMapper.toDto(testConversation)).thenReturn(testConversationDto);

        // Act
        Optional<ConversationDto> result = conversationService.updateTitle(1L, "Updated Title", 1L);

        // Assert
        assertTrue(result.isPresent());
        verify(conversationRepository, times(1)).findById(1L);
        verify(conversationRepository, times(1)).save(any(Conversation.class));
        verify(auditService, times(1)).log(anyLong(), anyString(), anyString(), anyLong(), any(), any());
    }

    @Test
    void testUpdateTitleConversationNotFound() {
        // Arrange
        when(conversationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<ConversationDto> result = conversationService.updateTitle(999L, "Updated Title", 1L);

        // Assert
        assertFalse(result.isPresent());
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    void testGetActiveConversationCount() {
        // Arrange
        when(conversationRepository.countActiveByBotId(1L)).thenReturn(5L);

        // Act
        long result = conversationService.getActiveConversationCount(1L);

        // Assert
        assertEquals(5L, result);
        verify(conversationRepository, times(1)).countActiveByBotId(1L);
    }

    @Test
    void testFindByBotIdAndStatus() {
        // Arrange
        Page<Conversation> conversationPage = new PageImpl<>(List.of(testConversation));
        when(conversationRepository.findByBotIdAndStatus(eq(1L), eq("ACTIVE"), any())).thenReturn(conversationPage);
        when(conversationMapper.toDto(testConversation)).thenReturn(testConversationDto);

        // Act
        Page<ConversationDto> result = conversationService.findByBotIdAndStatus(1L, "ACTIVE", PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
