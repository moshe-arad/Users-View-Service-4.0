package org.moshe.arad.kafka.consumers.config.commands;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class GetUsersUpdateViewCommandConfig extends SimpleConsumerConfig{

	public GetUsersUpdateViewCommandConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GET_USERS_UPDATE_VIEW_COMMAND_GROUP);
	}
}
