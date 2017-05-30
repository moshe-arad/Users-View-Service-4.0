package org.moshe.arad.kafka.consumers.commands;

import java.io.IOException;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.CheckUserEmailCommand;
import org.moshe.arad.kafka.events.UserEmailAckEvent;
import org.moshe.arad.services.UsersView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("CheckUserEmailAvailabilityCommandConsumer")
@Scope("prototype")
public class CheckUserEmailCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(CheckUserEmailCommandConsumer.class);
	
	@Autowired
	private UsersView usersView;
	
	public CheckUserEmailCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		CheckUserEmailCommand checkUserEmailAvailabilityCommand = convertJsonBlobIntoEvent(record.value());
		logger.info("Check User Email Availability Command record recieved, " + record.value());
    	logger.info("Checking whether email occupied...");
    	boolean isAvailable = usersView.isEmailAvailable(checkUserEmailAvailabilityCommand.getEmail());
    	logger.info("Email isAvailable = " + isAvailable + ", record = " + record.value());
    	UserEmailAckEvent userEmailAvailabilityCheckedEvent = 
    			new UserEmailAckEvent(checkUserEmailAvailabilityCommand.getUuid(), 4, 4, new Date(),"UserEmailAvailabilityCheckedEvent", isAvailable);
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
	
	private CheckUserEmailCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, CheckUserEmailCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	