package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterAddSecondPlayerEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterAddSecondPlayerEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATED_ADDED_SECOND_PLAYER_EVENT_GROUP);
	}
}
