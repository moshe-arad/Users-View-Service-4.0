package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATE_AFTER_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_GROUP);
	}
}
