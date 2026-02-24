package com.br.chatbotplatformskeleton.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Envia notificação de nova conversa
     */
    public void sendNewConversationNotification(String userEmail, String botName, Long conversationId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userEmail", userEmail);
        data.put("botName", botName);
        data.put("conversationId", conversationId);
        data.put("subject", "Nova conversa iniciada: " + botName);
        data.put("template", "new_conversation");

        sendEmail(data);
    }

    /**
     * Envia relatório de conversa para moderação
     */
    public void sendConversationReportEmail(String moderatorEmail, String conversationTitle,
                                            Long conversationId, String reason) {
        Map<String, Object> data = new HashMap<>();
        data.put("moderatorEmail", moderatorEmail);
        data.put("conversationTitle", conversationTitle);
        data.put("conversationId", conversationId);
        data.put("reason", reason);
        data.put("subject", "Conversa marcada para revisão: " + conversationTitle);
        data.put("template", "conversation_flagged");

        sendEmail(data);
    }

    /**
     * Envia alerta de bot desativado
     */
    public void sendBotDeactivatedAlert(String adminEmail, String botName, Long botId) {
        Map<String, Object> data = new HashMap<>();
        data.put("adminEmail", adminEmail);
        data.put("botName", botName);
        data.put("botId", botId);
        data.put("subject", "Bot desativado: " + botName);
        data.put("template", "bot_deactivated");

        sendEmail(data);
    }

    /**
     * Envia resumo diário de atividade
     */
    public void sendDailySummaryReport(String adminEmail, Map<String, Object> stats) {
        Map<String, Object> data = new HashMap<>();
        data.put("adminEmail", adminEmail);
        data.put("stats", stats);
        data.put("subject", "Relatório Diário - Plataforma de Chatbots");
        data.put("template", "daily_summary");

        sendEmail(data);
    }

    /**
     * Método genérico para envio de email
     * TODO: Implementar com JavaMailSender ou SendGrid
     */
    private void sendEmail(Map<String, Object> emailData) {
        // Implementação futura com JavaMailSender
        // SmtpTemplate: emailData.get("template")
        // Para, Assunto, dados já definidos acima

        logger.info("Email queue: {} → {}",
            emailData.get("template"),
            emailData.get("adminEmail") != null ? emailData.get("adminEmail") : emailData.get("userEmail"));

        // Em produção, usar:
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setTo((String) emailData.get("email"));
        // message.setSubject((String) emailData.get("subject"));
        // mailSender.send(message);
    }
}

