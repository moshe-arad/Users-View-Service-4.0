package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionsUpdateAfterLoggedOutWatcherLeftEventConfig extends SimpleConsumerConfig{

	public UserPermissionsUpdateAfterLoggedOutWatcherLeftEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_PERMISSIONS_UPDATE_AFTER_LOGGED_OUT_WATCHER_LEFT_EVENT_GROUP);
	}
}
