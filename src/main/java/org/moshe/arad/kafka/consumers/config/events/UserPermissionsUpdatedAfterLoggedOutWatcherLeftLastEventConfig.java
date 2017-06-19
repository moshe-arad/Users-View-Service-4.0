package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATE_AFTER_LOGGED_OUT_WATCHER_LEFT_LAST_EVENT_GROUP);
	}
}
