package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterLoggedOutOpenByLeftEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterLoggedOutOpenByLeftEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATE_AFTER_LOGGED_OUT_OPENBY_LEFT_EVENT_GROUP);
	}
}
