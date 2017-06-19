package org.moshe.arad.kafka.consumers.config.commands;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component("CheckUserNameAvailabilityCommandConfig")
public class CheckUserNameCommandConfig extends SimpleConsumerConfig{

	public CheckUserNameCommandConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.CHECK_USER_NAME_AVAILABILITY_GROUP);
	}
}
