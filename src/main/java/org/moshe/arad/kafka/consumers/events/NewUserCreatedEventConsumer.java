package org.moshe.arad.kafka.consumers.events;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.UsersView;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.events.NewUserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class NewUserCreatedEventConsumer extends SimpleBackgammonEventsConsumer<NewUserCreatedEvent> {

	@Autowired
	private UsersView usersView;
	
	Logger logger = LoggerFactory.getLogger(NewUserCreatedEventConsumer.class);
	
	public NewUserCreatedEventConsumer(SimpleConsumerConfig simpleConsumerConfig, String topic) {
		super(simpleConsumerConfig, topic);
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,NewUserCreatedEvent> record) {
		logger.info("New User Created Event record recieved, " + record.value().getBackgammonUser());	             	                		               
    	NewUserCreatedEvent newUserCreatedEvent = (NewUserCreatedEvent)record.value();
    	logger.info("Updating user names in redis data store...");
    	usersView.addUserName(newUserCreatedEvent.getBackgammonUser().getUserName());
    	logger.info("Update completed...");
    	logger.info("Updating emailsin redis data store...");
    	usersView.addEmail(newUserCreatedEvent.getBackgammonUser().getEmail());
    	logger.info("Update completed...");		
	}	
}




	