package com.br.chatbotplatformskeleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChatbotPlatformSkeletonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatbotPlatformSkeletonApplication.class, args);
    }

}
