package org.moshe.arad.kafka.producers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component("UserNameAvailabilityCheckedConfig")
public class UserNameAvailabilityCheckedConfig extends SimpleProducerConfig{

	public UserNameAvailabilityCheckedConfig() {
		super();
		super.getProperties().put("value.serializer", KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_SERIALIZER);
	}
}
