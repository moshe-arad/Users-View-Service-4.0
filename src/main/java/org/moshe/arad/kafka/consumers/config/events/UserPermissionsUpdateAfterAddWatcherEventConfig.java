package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdateAfterAddWatcherEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdateAfterAddWatcherEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATED_ADDED_WATCHER_EVENT_GROUP);
	}
}
