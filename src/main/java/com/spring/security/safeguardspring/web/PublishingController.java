package com.spring.security.safeguardspring.web;

import com.spring.security.safeguardspring.service.MessagePublisher;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublishingController {

	private final @NonNull
	@Qualifier("kafkaMessagePublisher") MessagePublisher kafkaMessagePublisher;

	@GetMapping("/kafka")
	public String messagePublisher(@RequestParam String userName, @RequestParam String key) {
		try {
			kafkaMessagePublisher.sendMessage("Hello " + userName,key);
		} catch (Exception exception) {
			log.error(" Error while sending message to topic");
		}
		return "Successfully sent the message to kafka";
	}
}
