package org.moshe.arad.kafka.consumers.config.commands;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class LogOutUserCommandConfig extends SimpleConsumerConfig{

	public LogOutUserCommandConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LOG_OUT_USER_COMMAND_CONFIG_GROUP);
	}
}
