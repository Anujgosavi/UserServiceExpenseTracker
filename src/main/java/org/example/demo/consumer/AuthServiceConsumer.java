package org.example.demo.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.demo.entities.UserInfoDto;
import org.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthServiceConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceConsumer.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(UserInfoDto eventData) {
        log.info("AuthServiceConsumer: Received event for user: {}", eventData);
        try {
            userService.createOrUpdateUser(eventData);
            log.info("AuthServiceConsumer: Successfully processed user: {}", eventData);
        } catch (Exception ex) {
            log.error("AuthServiceConsumer: Exception while consuming kafka event", ex);
        }
    }
}