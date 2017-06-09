package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterCreateRoomEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterCreateRoomEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATE_AFTER_CREATE_ROOM_EVENT_GROUP);
	}
}
