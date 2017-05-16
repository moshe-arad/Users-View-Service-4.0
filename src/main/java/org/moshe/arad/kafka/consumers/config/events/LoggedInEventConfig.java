package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class LoggedInEventConfig extends SimpleConsumerConfig{

	public LoggedInEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LOGGED_IN_EVENT_GROUP);
	}
}
