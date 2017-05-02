package org.moshe.arad.kafka.consumers.config.events;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class ExistingUserJoinedLobbyEventConfig extends SimpleConsumerConfig{

	public ExistingUserJoinedLobbyEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.NEW_USER_JOINED_LOBBY_EVENT_GROUP2);
	}
}
