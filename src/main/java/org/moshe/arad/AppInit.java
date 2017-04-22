package org.moshe.arad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.CheckUserEmailAvailabilityCommand;
import org.moshe.arad.kafka.consumers.commands.CheckUserEmailAvailabilityCommandConsumer;
import org.moshe.arad.kafka.consumers.commands.CheckUserNameAvailabilityCommandConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.events.NewUserCreatedEventConsumer;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.moshe.arad.kafka.producers.SimpleBackgammonEventsProducer;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppInit implements ApplicationContextAware {
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(UsersView.class);
	
	private ApplicationContext context;
	
	@Autowired
	private NewUserCreatedEventConsumer newUserCreatedEventConsumer;
	
	@Resource(name = "NewUserCreatedEventConfig")
	private SimpleConsumerConfig newUserCreatedEventConfig;
	
	@Autowired
	private CheckUserNameAvailabilityCommandConsumer checkUserNameAvailabilityCommandConsumer;	
	
	@Resource(name = "CheckUserNameAvailabilityCommandConfig")
	private SimpleConsumerConfig checkUserNameAvailabilityCommandConfig;
	
	@Autowired
	private SimpleBackgammonEventsProducer<UserNameAvailabilityCheckedEvent> userNameAvailabilityCheckedProducer;
	
	@Resource(name = "UserNameAvailabilityCheckedConfig")
	private SimpleProducerConfig userNameAvailabilityCheckedConfig;
	
	@Resource(name = "CheckUserEmailAvailabilityCommandConsumer")
	private CheckUserEmailAvailabilityCommandConsumer checkUserEmailAvailabilityCommandConsumer;
	
	@Resource(name = "CheckUserEmailAvailabilityCommandConfig")
	private SimpleConsumerConfig checkUserEmailAvailabilityCommandConfig;
	
	@Autowired
	private SimpleBackgammonEventsProducer<UserEmailAvailabilityCheckedEvent> userEmailAvailabilityCheckedProducer;
	
	@Resource(name = "UserEmailAvailabilityCheckedConfig")
	private SimpleProducerConfig userEmailAvailabilityCheckedConfig;
	
	public void acceptNewEvents(){
		logger.info("Started to accept new events from services...");
		
		newUserCreatedEventConsumer.setTopic(KafkaUtils.NEW_USER_CREATED_EVENT_TOPIC);
		newUserCreatedEventConsumer.setSimpleConsumerConfig(newUserCreatedEventConfig);
		newUserCreatedEventConsumer.initConsumer();
		executor.execute(newUserCreatedEventConsumer);
		
		ConsumerToProducerQueue consumerToProducerQueue = context.getBean(ConsumerToProducerQueue.class);
		
		checkUserNameAvailabilityCommandConsumer.setTopic(KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC);
		checkUserNameAvailabilityCommandConsumer.setSimpleConsumerConfig(checkUserNameAvailabilityCommandConfig);
		checkUserNameAvailabilityCommandConsumer.initConsumer();
		checkUserNameAvailabilityCommandConsumer.setConsumerToProducerQueue(consumerToProducerQueue);
		executor.execute(checkUserNameAvailabilityCommandConsumer);
		
		userNameAvailabilityCheckedProducer.setTopic(KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC);
		userNameAvailabilityCheckedProducer.setSimpleProducerConfig(userNameAvailabilityCheckedConfig);
		userNameAvailabilityCheckedProducer.setConsumerToProducerQueue(consumerToProducerQueue);
		executor.execute(userNameAvailabilityCheckedProducer);
		
		consumerToProducerQueue = null;
		consumerToProducerQueue = context.getBean(ConsumerToProducerQueue.class);
		
		checkUserEmailAvailabilityCommandConsumer.setTopic(KafkaUtils.CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC);
		checkUserEmailAvailabilityCommandConsumer.setSimpleConsumerConfig(checkUserEmailAvailabilityCommandConfig);
		checkUserEmailAvailabilityCommandConsumer.initConsumer();
		checkUserEmailAvailabilityCommandConsumer.setConsumerToProducerQueue(consumerToProducerQueue);
		executor.execute(checkUserEmailAvailabilityCommandConsumer);
		
		userEmailAvailabilityCheckedProducer.setTopic(KafkaUtils.EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC);
		userEmailAvailabilityCheckedProducer.setSimpleProducerConfig(userEmailAvailabilityCheckedConfig);
		userEmailAvailabilityCheckedProducer.setConsumerToProducerQueue(consumerToProducerQueue);
		executor.execute(userEmailAvailabilityCheckedProducer);
		logger.info("Stopped to accept new events from services...");
	}
	
	public void shutdown(){
		newUserCreatedEventConsumer.setRunning(false);
		newUserCreatedEventConsumer.getScheduledExecutor().shutdown();
		
		this.executor.shutdown();
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
}
