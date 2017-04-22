package org.moshe.arad.kafka.consumers.commands;

import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.UsersView;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.CheckUserEmailAvailabilityCommand;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CheckUserEmailAvailabilityCommandConsumer")
public class CheckUserEmailAvailabilityCommandConsumer extends SimpleBackgammonCommandsConsumer<CheckUserEmailAvailabilityCommand> {

	private ConsumerToProducerQueue consumerToProducerQueue;
	
	@Autowired
	private UsersView usersView;
	
	public CheckUserEmailAvailabilityCommandConsumer() {
	}
	
	public CheckUserEmailAvailabilityCommandConsumer(SimpleConsumerConfig simpleConsumerConfig, String topic) {
		super(simpleConsumerConfig, topic);
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, CheckUserEmailAvailabilityCommand> record) {
		logger.info("Check User Email Availability Command record recieved, " + record.value());
    	logger.info("Checking whether email occupied...");
    	boolean isAvailable = usersView.isEmailAvailable(record.value().getEmail());
    	logger.info("Email isAvailable = " + isAvailable + ", record = " + record.value());
    	UserEmailAvailabilityCheckedEvent userEmailAvailabilityCheckedEvent = 
    			new UserEmailAvailabilityCheckedEvent(record.value().getUuid(), 4,"Users View Service", 1, "User", 4, "UserEmailAvailabilityCheckedEvent", new Date(), isAvailable);
    	logger.info("passing email availability checked event to producer...");
    	consumerToProducerQueue.getEventsQueue().put(userEmailAvailabilityCheckedEvent);
    	logger.info("Event passed to producer...");		
	}

	public ConsumerToProducerQueue getConsumerToProducerQueue() {
		return consumerToProducerQueue;
	}

	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}
}




	