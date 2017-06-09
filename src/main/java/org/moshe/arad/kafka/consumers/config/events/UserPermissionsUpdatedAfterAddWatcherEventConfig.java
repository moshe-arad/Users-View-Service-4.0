package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterAddWatcherEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterAddWatcherEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATED_ADDED_WATCHER_EVENT_GROUP);
	}
}
