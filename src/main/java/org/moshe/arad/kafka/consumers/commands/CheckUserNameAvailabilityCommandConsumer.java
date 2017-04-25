package org.moshe.arad.kafka.consumers.commands;

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
import org.moshe.arad.UsersView;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckUserNameAvailabilityCommandConsumer extends SimpleBackgammonCommandsConsumer<CheckUserNameAvailabilityCommand> {

	private ConsumerToProducerQueue consumerToProducerQueue;
	
	@Autowired
	private UsersView usersView;
	
	public CheckUserNameAvailabilityCommandConsumer() {
	}
	
	public CheckUserNameAvailabilityCommandConsumer(SimpleConsumerConfig simpleConsumerConfig, String topic) {
		super(simpleConsumerConfig, topic);
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, CheckUserNameAvailabilityCommand> record) {
		logger.info("Check User Name Availability Command record recieved, " + record.value());
    	logger.info("Checking whether user name occupied...");
    	boolean isAvailable = usersView.isUserNameAvailable(record.value().getUserName());
    	logger.info("User Name = " + record.value().getUserName() + " , isAvailable = " + isAvailable);
    	UserNameAvailabilityCheckedEvent userNameAvailabilityCheckedEvent = 
    			new UserNameAvailabilityCheckedEvent(record.value().getUuid(), 4, 3, new Date(), isAvailable);
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
}




	