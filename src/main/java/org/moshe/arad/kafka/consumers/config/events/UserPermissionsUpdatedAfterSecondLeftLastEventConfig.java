package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterSecondLeftLastEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterSecondLeftLastEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATED_AFTER_SECOND_LEFT_LAST_EVENT_GROUP);
	}
}
