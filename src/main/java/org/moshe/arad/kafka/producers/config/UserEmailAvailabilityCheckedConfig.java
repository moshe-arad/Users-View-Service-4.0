package org.moshe.arad.kafka.producers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component("UserEmailAvailabilityCheckedConfig")
public class UserEmailAvailabilityCheckedConfig extends SimpleProducerConfig{

	public UserEmailAvailabilityCheckedConfig() {
		super();
		super.getProperties().put("value.serializer", KafkaUtils.USER_EMAIL_AVAILABILITY_CHECKED_EVENT_SERIALIZER);
	}
}
