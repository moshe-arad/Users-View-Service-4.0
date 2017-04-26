package org.moshe.arad.initializer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.commands.CheckUserEmailAvailabilityCommandConsumer;
import org.moshe.arad.kafka.consumers.commands.CheckUserNameAvailabilityCommandConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.events.NewUserCreatedEventConsumer;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.moshe.arad.kafka.producers.ISimpleProducer;
import org.moshe.arad.kafka.producers.SimpleEventsProducer;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.moshe.arad.services.UsersView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppInit implements ApplicationContextAware, IAppInitializer {	
	
	@Autowired
	private NewUserCreatedEventConsumer newUserCreatedEventConsumer;
	
	@Resource(name = "NewUserCreatedEventConfig")
	private SimpleConsumerConfig newUserCreatedEventConfig;
	
	@Autowired
	private CheckUserNameAvailabilityCommandConsumer checkUserNameAvailabilityCommandConsumer;	
	
	@Resource(name = "CheckUserNameAvailabilityCommandConfig")
	private SimpleConsumerConfig checkUserNameAvailabilityCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<UserNameAvailabilityCheckedEvent> userNameAvailabilityCheckedEventProducer;
	
	@Resource(name = "UserNameAvailabilityCheckedConfig")
	private SimpleProducerConfig userNameAvailabilityCheckedConfig;
	
	@Resource(name = "CheckUserEmailAvailabilityCommandConsumer")
	private CheckUserEmailAvailabilityCommandConsumer checkUserEmailAvailabilityCommandConsumer;
	
	@Resource(name = "CheckUserEmailAvailabilityCommandConfig")
	private SimpleConsumerConfig checkUserEmailAvailabilityCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<UserEmailAvailabilityCheckedEvent> userEmailAvailabilityCheckedEventProducer;
	
	@Resource(name = "UserEmailAvailabilityCheckedConfig")
	private SimpleProducerConfig userEmailAvailabilityCheckedConfig;
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(UsersView.class);
	
	private ApplicationContext context;
	
	private ConsumerToProducerQueue userNameconsumerToProducerQueue = null;
	
	private ConsumerToProducerQueue userEmailconsumerToProducerQueue = null;
	
	@Override
	public void initKafkaCommandsConsumers() {
		userNameconsumerToProducerQueue = context.getBean(ConsumerToProducerQueue.class);
		
		logger.info("Initializing new user created event consumer...");
		initSingleConsumer(checkUserNameAvailabilityCommandConsumer, KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC, checkUserNameAvailabilityCommandConfig, userNameconsumerToProducerQueue);
		logger.info("Initialize new user created event, completed...");
		
		userEmailconsumerToProducerQueue = context.getBean(ConsumerToProducerQueue.class);
		
		logger.info("Initializing new user created event consumer...");
		initSingleConsumer(checkUserEmailAvailabilityCommandConsumer, KafkaUtils.CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC, checkUserEmailAvailabilityCommandConfig, userEmailconsumerToProducerQueue);
		logger.info("Initialize new user created event, completed...");
		
		executeProducersAndConsumers(Arrays.asList(checkUserNameAvailabilityCommandConsumer, checkUserEmailAvailabilityCommandConsumer));
	}

	@Override
	public void initKafkaEventsConsumers() {
		logger.info("Initializing new user created event consumer...");
		initSingleConsumer(newUserCreatedEventConsumer, KafkaUtils.NEW_USER_CREATED_EVENT_TOPIC, newUserCreatedEventConfig, null);
		logger.info("Initialize new user created event, completed...");
		
		executeProducersAndConsumers(Arrays.asList(newUserCreatedEventConsumer));		
	}

	@Override
	public void initKafkaCommandsProducers() {
		
	}

	@Override
	public void initKafkaEventsProducers() {
		logger.info("Initializing new user created event consumer...");
		initSingleProducer(userNameAvailabilityCheckedEventProducer, KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC, userNameAvailabilityCheckedConfig, userNameconsumerToProducerQueue);
		logger.info("Initialize new user created event, completed...");
		
		logger.info("Initializing new user created event consumer...");
		initSingleProducer(userEmailAvailabilityCheckedEventProducer, KafkaUtils.EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC, userEmailAvailabilityCheckedConfig, userEmailconsumerToProducerQueue);
		logger.info("Initialize new user created event, completed...");
		
		executeProducersAndConsumers(Arrays.asList(userNameAvailabilityCheckedEventProducer, userEmailAvailabilityCheckedEventProducer));		
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void engineShutdown() {
		logger.info("about to do shutdown.");	
		shutdownSingleConsumer(checkUserEmailAvailabilityCommandConsumer);
		shutdownSingleConsumer(checkUserNameAvailabilityCommandConsumer);
		shutdownSingleConsumer(newUserCreatedEventConsumer);		
		shutdownSingleProducer(userNameAvailabilityCheckedEventProducer);
		shutdownSingleProducer(userEmailAvailabilityCheckedEventProducer);
		selfShutdown();
		logger.info("shutdown compeleted.");
	}	
	
	private void initSingleConsumer(ISimpleConsumer consumer, String topic, SimpleConsumerConfig consumerConfig, ConsumerToProducerQueue queue) {
		consumer.setTopic(topic);
		consumer.setSimpleConsumerConfig(consumerConfig);
		consumer.initConsumer();	
		consumer.setConsumerToProducerQueue(queue);
	}
	
	private void initSingleProducer(ISimpleProducer producer, String topic, SimpleProducerConfig consumerConfig, ConsumerToProducerQueue queue) {
		producer.setTopic(topic);
		producer.setSimpleProducerConfig(consumerConfig);	
		producer.setConsumerToProducerQueue(queue);
	}
	
	private void shutdownSingleConsumer(ISimpleConsumer consumer) {
		consumer.setRunning(false);
		consumer.getScheduledExecutor().shutdown();	
	}
	
	private void shutdownSingleProducer(ISimpleProducer producer) {
		producer.setRunning(false);
		producer.getScheduledExecutor().shutdown();	
	}
	
	private void selfShutdown(){
		this.executor.shutdown();
	}
	
	private void executeProducersAndConsumers(List<Runnable> jobs){
		for(Runnable job:jobs)
			executor.execute(job);
	}
}
