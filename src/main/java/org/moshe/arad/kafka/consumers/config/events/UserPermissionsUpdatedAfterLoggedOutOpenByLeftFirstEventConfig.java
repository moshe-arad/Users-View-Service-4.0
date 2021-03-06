package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATED_AFTER_LOGGED_OUT_OPENBY_LEFT_FIRST_EVENT_GROUP);
	}
}
