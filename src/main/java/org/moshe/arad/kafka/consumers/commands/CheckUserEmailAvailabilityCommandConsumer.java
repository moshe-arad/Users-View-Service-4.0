package org.moshe.arad.kafka.consumers.commands;

import java.io.IOException;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.CheckUserEmailAvailabilityCommand;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.events.NewUserCreatedEvent;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.services.UsersView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("CheckUserEmailAvailabilityCommandConsumer")
public class CheckUserEmailAvailabilityCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(CheckUserEmailAvailabilityCommandConsumer.class);
	
	@Autowired
	private UsersView usersView;
	
	public CheckUserEmailAvailabilityCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		CheckUserEmailAvailabilityCommand checkUserEmailAvailabilityCommand = convertJsonBlobIntoEvent(record.value());
		logger.info("Check User Email Availability Command record recieved, " + record.value());
    	logger.info("Checking whether email occupied...");
    	boolean isAvailable = usersView.isEmailAvailable(checkUserEmailAvailabilityCommand.getEmail());
    	logger.info("Email isAvailable = " + isAvailable + ", record = " + record.value());
    	UserEmailAvailabilityCheckedEvent userEmailAvailabilityCheckedEvent = 
    			new UserEmailAvailabilityCheckedEvent(checkUserEmailAvailabilityCommand.getUuid(), 4, 4, new Date(),"UserEmailAvailabilityCheckedEvent", isAvailable);
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
	
	private CheckUserEmailAvailabilityCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, CheckUserEmailAvailabilityCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	