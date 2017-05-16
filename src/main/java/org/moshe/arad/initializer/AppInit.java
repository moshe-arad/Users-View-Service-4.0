package org.moshe.arad.initializer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.commands.CheckUserEmailCommandConsumer;
import org.moshe.arad.kafka.consumers.commands.CheckUserNameCommandConsumer;
import org.moshe.arad.kafka.consumers.commands.LogOutUserCommandConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.config.commands.LogInUserCommandConfig;
import org.moshe.arad.kafka.consumers.config.commands.LogOutUserCommandConfig;
import org.moshe.arad.kafka.consumers.config.events.ExistingUserJoinedLobbyEventConfig;
import org.moshe.arad.kafka.consumers.config.events.LoggedInEventConfig;
import org.moshe.arad.kafka.consumers.config.events.NewUserJoinedLobbyEventConfig;
import org.moshe.arad.kafka.consumers.events.ExistingUserJoinedLobbyEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedInEventConsumer;
import org.moshe.arad.kafka.consumers.events.NewUserCreatedEventConsumer;
import org.moshe.arad.kafka.consumers.events.NewUserJoinedLobbyEventConsumer;
import org.moshe.arad.kafka.events.LogInUserAckEvent;
import org.moshe.arad.kafka.events.LogOutUserAckEvent;
import org.moshe.arad.kafka.events.LoggedInEventAck;
import org.moshe.arad.kafka.events.NewUserCreatedEventAck;
import org.moshe.arad.kafka.events.UserEmailAckEvent;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.kafka.producers.ISimpleProducer;
import org.moshe.arad.kafka.producers.events.SimpleEventsProducer;
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
	
	private NewUserCreatedEventConsumer newUserCreatedEventConsumer;
	
	@Resource(name = "NewUserCreatedEventConfig")
	private SimpleConsumerConfig newUserCreatedEventConfig;
	
	private CheckUserNameCommandConsumer checkUserNameAvailabilityCommandConsumer;	
	
	@Resource(name = "CheckUserNameAvailabilityCommandConfig")
	private SimpleConsumerConfig checkUserNameAvailabilityCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<UserNameAckEvent> userNameAvailabilityCheckedEventProducer;
	
	private CheckUserEmailCommandConsumer checkUserEmailAvailabilityCommandConsumer;
	
	@Resource(name = "CheckUserEmailAvailabilityCommandConfig")
	private SimpleConsumerConfig checkUserEmailAvailabilityCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<UserEmailAckEvent> userEmailAvailabilityCheckedEventProducer;
	
	private NewUserJoinedLobbyEventConsumer newUserJoinedLobbyEventConsumer;
	
	@Autowired
	private NewUserJoinedLobbyEventConfig newUserJoinedLobbyEventConfig;
	
//	private LogInUserCommandConsumer logInUserCommandConsumer;
	
	@Autowired
	private LogInUserCommandConfig logInUserCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<LogInUserAckEvent> logInUserAckEventProducer;
	
	private ExistingUserJoinedLobbyEventConsumer existingUserJoinedLobbyEventConsumer;
	
	@Autowired
	private ExistingUserJoinedLobbyEventConfig existingUserJoinedLobbyEventConfig;
	
	private LogOutUserCommandConsumer logOutUserCommandConsumer;
	
	@Autowired
	private LogOutUserCommandConfig logOutUserCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<LogOutUserAckEvent> logOutUserAckEventProducer;
	
	@Autowired
	private SimpleEventsProducer<NewUserCreatedEventAck> newUserCreatedEventAckProducer;
	
	private LoggedInEventConsumer loggedInEventConsumer;
	
	@Autowired
	private LoggedInEventConfig loggedInEventConfig;
	
	@Autowired
	private SimpleEventsProducer<LoggedInEventAck> loggedInEventAckProducer;
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(UsersView.class);
	
	private ApplicationContext context;
	
	private ConsumerToProducerQueue userNameconsumerToProducerQueue = null;
	
	private ConsumerToProducerQueue userEmailconsumerToProducerQueue = null;
	
	private ConsumerToProducerQueue logInUserCommandQueue = null;
	
	private ConsumerToProducerQueue logOutUserCommandQueue = null;
	
	private ConsumerToProducerQueue newUserCreatedEventAckQueue = null;
	
	private ConsumerToProducerQueue loggedInEventQueue = null;
	
	public static final int NUM_CONSUMERS = 5;
	
	@Override
	public void initKafkaCommandsConsumers() {
		userNameconsumerToProducerQueue = context.getBean(ConsumerToProducerQueue.class);
		userEmailconsumerToProducerQueue = context.getBean(ConsumerToProducerQueue.class);
		logInUserCommandQueue = context.getBean(ConsumerToProducerQueue.class);
		logOutUserCommandQueue = context.getBean(ConsumerToProducerQueue.class);
		
		for(int i=0; i<NUM_CONSUMERS; i++){
			checkUserNameAvailabilityCommandConsumer = context.getBean(CheckUserNameCommandConsumer.class);
			
			logger.info("Initializing new user created event consumer...");
			initSingleConsumer(checkUserNameAvailabilityCommandConsumer, KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC, checkUserNameAvailabilityCommandConfig, userNameconsumerToProducerQueue);
			logger.info("Initialize new user created event, completed...");
			
			checkUserEmailAvailabilityCommandConsumer = context.getBean(CheckUserEmailCommandConsumer.class);
			
			logger.info("Initializing new user created event consumer...");
			initSingleConsumer(checkUserEmailAvailabilityCommandConsumer, KafkaUtils.CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC, newUserJoinedLobbyEventConfig, userEmailconsumerToProducerQueue);
			logger.info("Initialize new user created event, completed...");
			
			
//			logInUserCommandConsumer = context.getBean(LogInUserCommandConsumer.class);
//			initSingleConsumer(logInUserCommandConsumer, KafkaUtils.LOG_IN_USER_COMMAND_TOPIC, logInUserCommandConfig, logInUserCommandQueue);
		
			logOutUserCommandConsumer = context.getBean(LogOutUserCommandConsumer.class);
			initSingleConsumer(logOutUserCommandConsumer, KafkaUtils.LOG_OUT_USER_COMMAND_TOPIC, logOutUserCommandConfig, logOutUserCommandQueue);
			
			executeProducersAndConsumers(Arrays.asList(checkUserNameAvailabilityCommandConsumer, 
					checkUserEmailAvailabilityCommandConsumer,
//					logInUserCommandConsumer,
					logOutUserCommandConsumer));
		}
	}

	@Override
	public void initKafkaEventsConsumers() {
		newUserCreatedEventAckQueue = context.getBean(ConsumerToProducerQueue.class);
		loggedInEventQueue = context.getBean(ConsumerToProducerQueue.class);
		
		for(int i=0; i<NUM_CONSUMERS; i++){
			newUserCreatedEventConsumer = context.getBean(NewUserCreatedEventConsumer.class);
			newUserJoinedLobbyEventConsumer = context.getBean(NewUserJoinedLobbyEventConsumer.class);
			existingUserJoinedLobbyEventConsumer = context.getBean(ExistingUserJoinedLobbyEventConsumer.class);			
			loggedInEventConsumer = context.getBean(LoggedInEventConsumer.class);
			
			logger.info("Initializing new user created event consumer...");
			initSingleConsumer(newUserCreatedEventConsumer, KafkaUtils.NEW_USER_CREATED_EVENT_TOPIC, newUserCreatedEventConfig, newUserCreatedEventAckQueue);
			
			initSingleConsumer(newUserJoinedLobbyEventConsumer, KafkaUtils.NEW_USER_JOINED_LOBBY_EVENT_TOPIC, newUserCreatedEventConfig, null);
			logger.info("Initialize new user created event, completed...");
			
			initSingleConsumer(existingUserJoinedLobbyEventConsumer, KafkaUtils.EXISTING_USER_JOINED_LOBBY_EVENT_TOPIC, existingUserJoinedLobbyEventConfig, null);
			
			initSingleConsumer(loggedInEventConsumer, KafkaUtils.LOGGED_IN_EVENT_TOPIC, loggedInEventConfig, loggedInEventQueue);
			
			executeProducersAndConsumers(Arrays.asList(newUserCreatedEventConsumer, 
					newUserJoinedLobbyEventConsumer,
					existingUserJoinedLobbyEventConsumer,
					loggedInEventConsumer));
		}
	}

	@Override
	public void initKafkaCommandsProducers() {
		
	}

	@Override
	public void initKafkaEventsProducers() {
		logger.info("Initializing new user created event consumer...");
		initSingleProducer(userNameAvailabilityCheckedEventProducer, KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC, userNameconsumerToProducerQueue);
		logger.info("Initialize new user created event, completed...");
		
		logger.info("Initializing new user created event consumer...");
		initSingleProducer(userEmailAvailabilityCheckedEventProducer, KafkaUtils.EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC, userEmailconsumerToProducerQueue);
		logger.info("Initialize new user created event, completed...");
		
		initSingleProducer(logInUserAckEventProducer, KafkaUtils.LOG_IN_USER_ACK_EVENT_TOPIC, logInUserCommandQueue);
		
		initSingleProducer(logOutUserAckEventProducer, KafkaUtils.LOG_OUT_USER_ACK_EVENT_TOPIC, logOutUserCommandQueue);
		
		initSingleProducer(newUserCreatedEventAckProducer, KafkaUtils.NEW_USER_CREATED_EVENT_ACK_TOPIC, newUserCreatedEventAckQueue);
				
		initSingleProducer(loggedInEventAckProducer, KafkaUtils.LOGGED_IN_EVENT_ACK_TOPIC, loggedInEventQueue);
		executeProducersAndConsumers(Arrays.asList(userNameAvailabilityCheckedEventProducer, 
				userEmailAvailabilityCheckedEventProducer,
				logInUserAckEventProducer,
				logOutUserAckEventProducer,
				newUserCreatedEventAckProducer,
				loggedInEventAckProducer));		
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
	
	private void initSingleProducer(ISimpleProducer producer, String topic, ConsumerToProducerQueue queue) {
		producer.setTopic(topic);	
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
