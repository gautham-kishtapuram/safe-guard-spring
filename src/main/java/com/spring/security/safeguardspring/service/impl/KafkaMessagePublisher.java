package com.spring.security.safeguardspring.service.impl;

import com.spring.security.safeguardspring.service.MessagePublisher;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaMessagePublisher implements MessagePublisher {

	private final @NonNull KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void sendMessage(String message, String key ) {
		kafkaTemplate.send("kafka-architecture", key,message);

	}
}
