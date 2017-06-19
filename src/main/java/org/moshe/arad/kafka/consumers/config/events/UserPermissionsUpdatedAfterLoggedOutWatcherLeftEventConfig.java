package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterLoggedOutWatcherLeftEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterLoggedOutWatcherLeftEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATE_AFTER_LOGGED_OUT_WATCHER_LEFT_EVENT_GROUP);
	}
}
