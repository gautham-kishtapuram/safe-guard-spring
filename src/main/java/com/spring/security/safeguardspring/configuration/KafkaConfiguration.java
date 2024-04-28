package com.spring.security.safeguardspring.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfiguration {
	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		// props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
		// CustomKafkaJsonSerailizer.class);

		//		props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, KafkaProducerPartitioner.class);

		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
//		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "1000");
		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 16 * 1024 * 1024);
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
		props.put(ProducerConfig.RETRIES_CONFIG, 100);
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
		return props;
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		DefaultKafkaProducerFactory defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
		//		defaultKafkaProducerFactory.setValueSerializer(getCustomValueSerializer());
		return defaultKafkaProducerFactory;
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
