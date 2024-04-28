package com.spring.security.safeguardspring.service;

public interface MessagePublisher {

	void sendMessage(String message, String key);
}
