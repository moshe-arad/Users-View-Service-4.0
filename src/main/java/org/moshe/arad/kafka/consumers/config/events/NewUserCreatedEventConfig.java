package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component("NewUserCreatedEventConfig")
public class NewUserCreatedEventConfig extends SimpleConsumerConfig{

	public NewUserCreatedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.NEW_USER_CREATED_EVENT_GROUP);
		super.getProperties().put("value.deserializer", KafkaUtils.NEW_USER_CREATED_EVENT_DESERIALIZER);
	}
}
