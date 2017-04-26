package org.moshe.arad.kafka.consumers.config.commands;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component("CheckUserEmailAvailabilityCommandConfig")
public class CheckUserEmailAvailabilityCommandConfig extends SimpleConsumerConfig{

	public CheckUserEmailAvailabilityCommandConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.CHECK_USER_EMAIL_AVAILABILITY_GROUP);
	}
}
