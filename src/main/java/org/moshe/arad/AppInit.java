package org.moshe.arad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.commands.CheckUserNameAvailabilityCommandConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.events.NewUserCreatedEventConsumer;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.moshe.arad.kafka.producers.SimpleBackgammonEventsProducer;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppInit {
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(UsersView.class);
	
	@Autowired
	private NewUserCreatedEventConsumer newUserCreatedEventConsumer;
	
	@Autowired
	private CheckUserNameAvailabilityCommandConsumer checkUserNameAvailabilityCommandConsumer;
	
	@Resource(name = "NewUserCreatedEventConfig")
	private SimpleConsumerConfig newUserCreatedEventConfig;
	
	@Resource(name = "CheckUserNameAvailabilityCommandConfig")
	private SimpleConsumerConfig checkUserNameAvailabilityCommandConfig;
	
	@Autowired
	private SimpleBackgammonEventsProducer<UserNameAvailabilityCheckedEvent> userNameAvailabilityCheckedProducer;
	
	@Resource(name = "UserNameAvailabilityCheckedConfig")
	private SimpleProducerConfig userNameAvailabilityCheckedConfig;
	
	public void acceptNewEvents(){
		logger.info("Started to accept new events from services...");
		
		newUserCreatedEventConsumer.setTopic(KafkaUtils.NEW_USER_CREATED_EVENT_TOPIC);
		newUserCreatedEventConsumer.setSimpleConsumerConfig(newUserCreatedEventConfig);
		executor.execute(newUserCreatedEventConsumer);
		
		checkUserNameAvailabilityCommandConsumer.setTopic(KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC);
		checkUserNameAvailabilityCommandConsumer.setSimpleConsumerConfig(checkUserNameAvailabilityCommandConfig);
		executor.execute(checkUserNameAvailabilityCommandConsumer);
		
		userNameAvailabilityCheckedProducer.setTopic(KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC);
		userNameAvailabilityCheckedProducer.setSimpleProducerConfig(userNameAvailabilityCheckedConfig);
		executor.execute(userNameAvailabilityCheckedProducer);
		logger.info("Stopped to accept new events from services...");
	}
	
	public void shutdown(){
		newUserCreatedEventConsumer.setRunning(false);
		newUserCreatedEventConsumer.getScheduledExecutor().shutdown();
		
		this.executor.shutdown();
	}
}
