package org.moshe.arad.kafka.consumers.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.CheckUserEmailAvailabilityCommand;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.moshe.arad.services.UsersView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CheckUserNameAvailabilityCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(CheckUserNameAvailabilityCommandConsumer.class);
	
	@Autowired
	private UsersView usersView;
	
	public CheckUserNameAvailabilityCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		CheckUserNameAvailabilityCommand checkUserNameAvailabilityCommand = convertJsonBlobIntoEvent(record.value());
		logger.info("Check User Name Availability Command record recieved, " + record.value());
    	logger.info("Checking whether user name occupied...");
    	boolean isAvailable = usersView.isUserNameAvailable(checkUserNameAvailabilityCommand.getUserName());
    	logger.info("User Name = " + checkUserNameAvailabilityCommand.getUserName() + " , isAvailable = " + isAvailable);
    	UserNameAvailabilityCheckedEvent userNameAvailabilityCheckedEvent = 
    			new UserNameAvailabilityCheckedEvent(checkUserNameAvailabilityCommand.getUuid(), 4, 3, new Date(), isAvailable);
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
	
	private CheckUserNameAvailabilityCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, CheckUserNameAvailabilityCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	