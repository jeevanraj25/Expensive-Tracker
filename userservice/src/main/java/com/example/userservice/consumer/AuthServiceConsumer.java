package com.example.userservice.consumer;

import com.example.userservice.entities.UserInfo;
import com.example.userservice.entities.UserInfoDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceConsumer.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic-json.name}")
    private String topicName;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    public AuthServiceConsumer(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
//        logger.info("📢 AuthServiceConsumer initialized with topic: {} and group: {}", topicName, groupId);
    }

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(String message) {
//        logger.info("📥 Received message from Kafka: {}", message);

        try {
            UserInfoDto userInfoDto = objectMapper.readValue(message, UserInfoDto.class);
//            logger.info("📥 Successfully deserialized message to UserInfoDto: {}", userInfoDto);

            UserInfo userInfo = userInfoDto.transformToUserInfo();
//            logger.info("🔄 Transformed to UserInfo: {}", userInfo);

            userService.createOrUpdate(userInfoDto);
//            logger.info("✅ Successfully processed user data for userId: {}", userInfoDto.getUserId());
        } catch (Exception e) {
            logger.error("❌ Error processing Kafka message: {}", message, e);
        }
    }
}