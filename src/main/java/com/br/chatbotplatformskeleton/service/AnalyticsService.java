package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.repository.BotRepository;
import com.br.chatbotplatformskeleton.repository.ConversationRepository;
import com.br.chatbotplatformskeleton.repository.ConversationMessageRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final BotRepository botRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMessageRepository messageRepository;
    private final UserRepository userRepository;

    public AnalyticsService(BotRepository botRepository,
                          ConversationRepository conversationRepository,
                          ConversationMessageRepository messageRepository,
                          UserRepository userRepository) {
        this.botRepository = botRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("botCount", botRepository.count());
        stats.put("activeConversationCount", conversationRepository.count());
        stats.put("totalMessageCount", messageRepository.count());
        stats.put("userCount", userRepository.count());
        return stats;
    }

    public Map<String, Object> getBotAnalytics(Long botId) {
        Map<String, Object> analytics = new HashMap<>();

        long totalConversations = conversationRepository.countByBotId(botId);
        long activeConversations = conversationRepository.countActiveByBotId(botId);
        long totalMessages = messageRepository.countByBotId(botId);
        Double avgResponseTime = messageRepository.getAverageResponseTime(botId);
        Double avgSentimentScore = messageRepository.getAverageSentimentScore(botId);

        analytics.put("totalConversations", totalConversations);
        analytics.put("activeConversations", activeConversations);
        analytics.put("totalMessages", totalMessages);
        analytics.put("averageResponseTime", avgResponseTime != null ? avgResponseTime : 0.0);
        analytics.put("averageSentimentScore", avgSentimentScore != null ? avgSentimentScore : 0.0);

        return analytics;
    }

    public Map<String, Object> getSentimentAnalysis(Long botId) {
        Map<String, Object> sentiment = new HashMap<>();
        Double avgScore = messageRepository.getAverageSentimentScore(botId);

        sentiment.put("averageScore", avgScore != null ? avgScore : 0.0);
        sentiment.put("scale", "0.0 (negativo) a 1.0 (positivo)");

        return sentiment;
    }

    public Map<String, Object> getConversationMetrics(Long botId) {
        Map<String, Object> metrics = new HashMap<>();

        long totalConversations = conversationRepository.countByBotId(botId);
        long activeConversations = conversationRepository.countActiveByBotId(botId);
        long closedConversations = totalConversations - activeConversations;

        metrics.put("totalConversations", totalConversations);
        metrics.put("activeConversations", activeConversations);
        metrics.put("closedConversations", closedConversations);
        metrics.put("closedPercentage", totalConversations > 0 ?
            (closedConversations * 100.0 / totalConversations) : 0.0);

        return metrics;
    }
}
