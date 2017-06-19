package org.moshe.arad.kafka.consumers.commands;

import java.io.IOException;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.CheckUserNameCommand;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.view.utils.UsersView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class CheckUserNameCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(CheckUserNameCommandConsumer.class);
	
	@Autowired
	private UsersView usersView;
	
	public CheckUserNameCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		CheckUserNameCommand checkUserNameAvailabilityCommand = convertJsonBlobIntoEvent(record.value());
		logger.info("Check User Name Availability Command record recieved, " + record.value());
    	logger.info("Checking whether user name occupied...");
    	boolean isAvailable = usersView.isUserNameAvailable(checkUserNameAvailabilityCommand.getUserName());
    	logger.info("User Name = " + checkUserNameAvailabilityCommand.getUserName() + " , isAvailable = " + isAvailable);
    	UserNameAckEvent userNameAvailabilityCheckedEvent = 
    			new UserNameAckEvent(checkUserNameAvailabilityCommand.getUuid(), 4, 3, new Date(),"UserNameAvailabilityCheckedEvent", isAvailable);
    	logger.info("passing user name availability checked event to producer...");
    	consumerToProducerQueue.getEventsQueue().put(userNameAvailabilityCheckedEvent);
    	logger.info("Event passed to producer...");		
	}

	public ConsumerToProducerQueue getConsumerToProducerQueue() {
		return consumerToProducerQueue;
	}

	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}
	
	private CheckUserNameCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, CheckUserNameCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	