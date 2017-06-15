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
import org.moshe.arad.kafka.consumers.commands.GetUsersUpdateViewCommandConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.config.commands.GetUsersUpdateViewCommandConfig;
import org.moshe.arad.kafka.consumers.config.commands.LogInUserCommandConfig;
import org.moshe.arad.kafka.consumers.config.commands.LogOutUserCommandConfig;
import org.moshe.arad.kafka.consumers.config.events.ExistingUserJoinedLobbyEventConfig;
import org.moshe.arad.kafka.consumers.config.events.LoggedInEventConfig;
import org.moshe.arad.kafka.consumers.config.events.NewUserJoinedLobbyEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterAddSecondPlayerEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterAddWatcherEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterCreateRoomEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLeftLobbyEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutOpenByLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutSecondLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutWatcherLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterOpenByLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterOpenByLeftFirstEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterOpenByLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterSecondLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterSecondLeftFirstEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterWatcherLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.events.UserPermissionsUpdatedAfterWatcherLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.events.ExistingUserJoinedLobbyEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedInEventConsumer;
import org.moshe.arad.kafka.consumers.events.NewUserCreatedEventConsumer;
import org.moshe.arad.kafka.consumers.events.NewUserJoinedLobbyEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterAddSecondPlayerEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterAddWatcherEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterCreateRoomEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLeftLobbyEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutOpenByLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutSecondLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterOpenByLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterOpenByLeftFirstEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterOpenByLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterSecondLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterSecondLeftFirstEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterWatcherLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterWatcherLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutWatcherLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConsumer;
import org.moshe.arad.kafka.events.GetUsersUpdateViewAckEvent;
import org.moshe.arad.kafka.events.LogInUserAckEvent;
import org.moshe.arad.kafka.events.LogOutUserAckEvent;
import org.moshe.arad.kafka.events.LoggedInEventAck;
import org.moshe.arad.kafka.events.NewUserCreatedEventAck;
import org.moshe.arad.kafka.events.UserEmailAckEvent;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.kafka.producers.ISimpleProducer;
import org.moshe.arad.kafka.producers.events.SimpleEventsProducer;
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
	
	@Autowired
	private SimpleEventsProducer<LogInUserAckEvent> logInUserAckEventProducer;
	
	private ExistingUserJoinedLobbyEventConsumer existingUserJoinedLobbyEventConsumer;
	
	@Autowired
	private ExistingUserJoinedLobbyEventConfig existingUserJoinedLobbyEventConfig;
	
	@Autowired
	private SimpleEventsProducer<LogOutUserAckEvent> logOutUserAckEventProducer;
	
	@Autowired
	private SimpleEventsProducer<NewUserCreatedEventAck> newUserCreatedEventAckProducer;
	
	private LoggedInEventConsumer loggedInEventConsumer;
	
	@Autowired
	private LoggedInEventConfig loggedInEventConfig;
	
	@Autowired
	private SimpleEventsProducer<LoggedInEventAck> loggedInEventAckProducer;
	
	private GetUsersUpdateViewCommandConsumer getUsersUpdateViewCommandConsumer;
	
	@Autowired
	private GetUsersUpdateViewCommandConfig getUsersUpdateViewCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<GetUsersUpdateViewAckEvent> getUsersUpdateViewAckEventProducer;
	
	private UserPermissionsUpdatedAfterCreateRoomEventConsumer userPermissionsUpdatedAfterCreateRoomEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterCreateRoomEventConfig userPermissionsUpdateAfterCreateRoomEventConfig;
	
	private UserPermissionsUpdatedAfterAddWatcherEventConsumer userPermissionsUpdatedAfterAddWatcherEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterAddWatcherEventConfig userPermissionsUpdateAfterAddWatcherEventConfig;
	
	private UserPermissionsUpdatedAfterAddSecondPlayerEventConsumer userPermissionsUpdatedAfterAddSecondPlayerEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterAddSecondPlayerEventConfig userPermissionsUpdateAfterAddSecondPlayerEventConfig;
	
	private UserPermissionsUpdatedAfterLeftLobbyEventConsumer userPermissionsUpdatedAfterLeftLobbyEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLeftLobbyEventConfig userPermissionsUpdateAfterLeftLobbyEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConsumer userPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConfig userPermissionsUpdateAfterLoggedOutOpenByLeftBeforeGameStartedEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutOpenByLeftEventConsumer userPermissionsUpdatedAfterLoggedOutOpenByLeftEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutOpenByLeftEventConfig userPermissionsUpdateAfterLoggedOutOpenByLeftEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConsumer userPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConfig userPermissionsUpdateAfterLoggedOutWatcherLeftLastEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutWatcherLeftEventConsumer userPermissionsUpdatedAfterLoggedOutWatcherLeftEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutWatcherLeftEventConfig userPermissionsUpdateAfterLoggedOutWatcherLeftEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConsumer userPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConfig userPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConsumer userPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConfig userPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutSecondLeftEventConsumer userPermissionsUpdatedAfterLoggedOutSecondLeftEventConsumer;

	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutSecondLeftEventConfig userPermissionsUpdatedAfterLoggedOutSecondLeftEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConsumer userPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConfig userPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConfig;
	
	private UserPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConsumer userPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConfig userPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConfig;
	
	private UserPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConsumer userPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConfig userPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConfig;
	
	private UserPermissionsUpdatedAfterOpenByLeftEventConsumer userPermissionsUpdatedAfterOpenByLeftEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterOpenByLeftEventConfig userPermissionsUpdatedAfterOpenByLeftEventConfig;
	
	private UserPermissionsUpdatedAfterWatcherLeftLastEventConsumer userPermissionsUpdatedAfterWatcherLeftLastEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterWatcherLeftLastEventConfig userPermissionsUpdatedAfterWatcherLeftLastEventConfig;
	
	private UserPermissionsUpdatedAfterWatcherLeftEventConsumer userPermissionsUpdatedAfterWatcherLeftEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterWatcherLeftEventConfig userPermissionsUpdatedAfterWatcherLeftEventConfig;
	
	private UserPermissionsUpdatedAfterOpenByLeftFirstEventConsumer userPermissionsUpdatedAfterOpenByLeftFirstEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterOpenByLeftFirstEventConfig userPermissionsUpdatedAfterOpenByLeftFirstEventConfig;
	
	private UserPermissionsUpdatedAfterSecondLeftFirstEventConsumer userPermissionsUpdatedAfterSecondLeftFirstEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterSecondLeftFirstEventConfig userPermissionsUpdatedAfterSecondLeftFirstEventConfig;
	
	private UserPermissionsUpdatedAfterSecondLeftEventConsumer userPermissionsUpdatedAfterSecondLeftEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterSecondLeftEventConfig userPermissionsUpdatedAfterSecondLeftEventConfig;
	
	private UserPermissionsUpdatedAfterOpenByLeftLastEventConsumer userPermissionsUpdatedAfterOpenByLeftLastEventConsumer;
	
	@Autowired
	private UserPermissionsUpdatedAfterOpenByLeftLastEventConfig userPermissionsUpdatedAfterOpenByLeftLastEventConfig;
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(AppInit.class);
	
	private ApplicationContext context;
	
	private ConsumerToProducerQueue userNameconsumerToProducerQueue = null;
	
	private ConsumerToProducerQueue userEmailconsumerToProducerQueue = null;
	
	private ConsumerToProducerQueue logInUserCommandQueue = null;
	
	private ConsumerToProducerQueue logOutUserCommandQueue = null;
	
	private ConsumerToProducerQueue newUserCreatedEventAckQueue = null;
	
	private ConsumerToProducerQueue loggedInEventQueue = null;
	
	private ConsumerToProducerQueue usersUpdateViewQueue = null;
	
	public static final int NUM_CONSUMERS = 3;
	
	@Override
	public void initKafkaCommandsConsumers() {
		userNameconsumerToProducerQueue = context.getBean(ConsumerToProducerQueue.class);
		userEmailconsumerToProducerQueue = context.getBean(ConsumerToProducerQueue.class);
		logInUserCommandQueue = context.getBean(ConsumerToProducerQueue.class);
		logOutUserCommandQueue = context.getBean(ConsumerToProducerQueue.class);
		usersUpdateViewQueue = context.getBean(ConsumerToProducerQueue.class);

		for(int i=0; i<NUM_CONSUMERS; i++){
			checkUserNameAvailabilityCommandConsumer = context.getBean(CheckUserNameCommandConsumer.class);
			
			logger.info("Initializing new user created event consumer...");
			initSingleConsumer(checkUserNameAvailabilityCommandConsumer, KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC, checkUserNameAvailabilityCommandConfig, userNameconsumerToProducerQueue);
			logger.info("Initialize new user created event, completed...");
			
			checkUserEmailAvailabilityCommandConsumer = context.getBean(CheckUserEmailCommandConsumer.class);
			
			logger.info("Initializing new user created event consumer...");
			initSingleConsumer(checkUserEmailAvailabilityCommandConsumer, KafkaUtils.CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC, newUserJoinedLobbyEventConfig, userEmailconsumerToProducerQueue);
			logger.info("Initialize new user created event, completed...");
		
			getUsersUpdateViewCommandConsumer = context.getBean(GetUsersUpdateViewCommandConsumer.class);
			initSingleConsumer(getUsersUpdateViewCommandConsumer, KafkaUtils.GET_USERS_UPDATE_VIEW_COMMAND_TOPIC, getUsersUpdateViewCommandConfig, usersUpdateViewQueue);
			
			executeProducersAndConsumers(Arrays.asList(checkUserNameAvailabilityCommandConsumer, 
					checkUserEmailAvailabilityCommandConsumer,
					getUsersUpdateViewCommandConsumer
					));
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
			userPermissionsUpdatedAfterCreateRoomEventConsumer = context.getBean(UserPermissionsUpdatedAfterCreateRoomEventConsumer.class);
			userPermissionsUpdatedAfterAddWatcherEventConsumer = context.getBean(UserPermissionsUpdatedAfterAddWatcherEventConsumer.class);
			userPermissionsUpdatedAfterAddSecondPlayerEventConsumer = context.getBean(UserPermissionsUpdatedAfterAddSecondPlayerEventConsumer.class);
			userPermissionsUpdatedAfterLeftLobbyEventConsumer = context.getBean(UserPermissionsUpdatedAfterLeftLobbyEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutOpenByLeftEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutOpenByLeftEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutWatcherLeftEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutWatcherLeftEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutSecondLeftEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutSecondLeftEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConsumer.class);
			userPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConsumer = context.getBean(UserPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConsumer.class);
			userPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConsumer = context.getBean(UserPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConsumer.class);
			userPermissionsUpdatedAfterOpenByLeftEventConsumer = context.getBean(UserPermissionsUpdatedAfterOpenByLeftEventConsumer.class);
			userPermissionsUpdatedAfterWatcherLeftLastEventConsumer = context.getBean(UserPermissionsUpdatedAfterWatcherLeftLastEventConsumer.class);
			userPermissionsUpdatedAfterWatcherLeftEventConsumer = context.getBean(UserPermissionsUpdatedAfterWatcherLeftEventConsumer.class);
			userPermissionsUpdatedAfterOpenByLeftFirstEventConsumer = context.getBean(UserPermissionsUpdatedAfterOpenByLeftFirstEventConsumer.class);
			userPermissionsUpdatedAfterSecondLeftFirstEventConsumer = context.getBean(UserPermissionsUpdatedAfterSecondLeftFirstEventConsumer.class);
			userPermissionsUpdatedAfterSecondLeftEventConsumer = context.getBean(UserPermissionsUpdatedAfterSecondLeftEventConsumer.class);
			userPermissionsUpdatedAfterOpenByLeftLastEventConsumer = context.getBean(UserPermissionsUpdatedAfterOpenByLeftLastEventConsumer.class);
			
			logger.info("Initializing new user created event consumer...");
			initSingleConsumer(newUserCreatedEventConsumer, KafkaUtils.NEW_USER_CREATED_EVENT_TOPIC, newUserCreatedEventConfig, newUserCreatedEventAckQueue);
			
			initSingleConsumer(newUserJoinedLobbyEventConsumer, KafkaUtils.NEW_USER_JOINED_LOBBY_EVENT_TOPIC, newUserCreatedEventConfig, null);
			logger.info("Initialize new user created event, completed...");
			
			initSingleConsumer(existingUserJoinedLobbyEventConsumer, KafkaUtils.EXISTING_USER_JOINED_LOBBY_EVENT_TOPIC, existingUserJoinedLobbyEventConfig, null);
			
			initSingleConsumer(loggedInEventConsumer, KafkaUtils.LOGGED_IN_EVENT_TOPIC, loggedInEventConfig, loggedInEventQueue);
			
			initSingleConsumer(userPermissionsUpdatedAfterCreateRoomEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_EVENT_TOPIC, userPermissionsUpdateAfterCreateRoomEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterAddWatcherEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_USER_ADDED_WATCHER_EVENT_TOPIC, userPermissionsUpdateAfterAddWatcherEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterAddSecondPlayerEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_USER_ADDED_SECOND_PLAYER_EVENT_TOPIC, userPermissionsUpdateAfterAddSecondPlayerEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterLeftLobbyEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_USER_LEFT_LOBBY_EVENT_TOPIC, userPermissionsUpdateAfterLeftLobbyEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC, userPermissionsUpdateAfterLoggedOutOpenByLeftBeforeGameStartedEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutOpenByLeftEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_OPENBY_LEFT_EVENT_TOPIC, userPermissionsUpdateAfterLoggedOutOpenByLeftEventConfig, null);
		
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_WATCHER_LEFT_LAST_EVENT_TOPIC, userPermissionsUpdateAfterLoggedOutWatcherLeftLastEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutWatcherLeftEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_WATCHER_LEFT_EVENT_TOPIC, userPermissionsUpdateAfterLoggedOutWatcherLeftEventConfig, null);
	
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_OPENBY_LEFT_FIRST_EVENT_TOPIC, userPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_SECOND_LEFT_FIRST_EVENT_TOPIC, userPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutSecondLeftEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_SECOND_LEFT_EVENT_TOPIC, userPermissionsUpdatedAfterLoggedOutSecondLeftEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_OPENBY_LEFT_LAST_EVENT_TOPIC, userPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_LOGGED_OUT_SECOND_LEFT_LAST_EVENT_TOPIC, userPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC, userPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterOpenByLeftEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_OPENBY_LEFT_EVENT_TOPIC, userPermissionsUpdatedAfterOpenByLeftEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterWatcherLeftLastEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_WATCHER_LEFT_LAST_EVENT_TOPIC, userPermissionsUpdatedAfterWatcherLeftLastEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterWatcherLeftEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_WATCHER_LEFT_EVENT_TOPIC, userPermissionsUpdatedAfterWatcherLeftEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterOpenByLeftFirstEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_OPENBY_LEFT_FIRST_EVENT_TOPIC, userPermissionsUpdatedAfterOpenByLeftFirstEventConfig, null);
		
			initSingleConsumer(userPermissionsUpdatedAfterSecondLeftFirstEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_SECOND_LEFT_FIRST_EVENT_TOPIC, userPermissionsUpdatedAfterSecondLeftFirstEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterSecondLeftEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_SECOND_LEFT_EVENT_TOPIC, userPermissionsUpdatedAfterSecondLeftEventConfig, null);
			
			initSingleConsumer(userPermissionsUpdatedAfterOpenByLeftLastEventConsumer, KafkaUtils.USER_PERMISSIONS_UPDATED_OPENBY_LEFT_LAST_EVENT_TOPIC, userPermissionsUpdatedAfterOpenByLeftLastEventConfig, null);
			
			executeProducersAndConsumers(Arrays.asList(newUserCreatedEventConsumer, 
					newUserJoinedLobbyEventConsumer,
					existingUserJoinedLobbyEventConsumer,
					loggedInEventConsumer,
					userPermissionsUpdatedAfterCreateRoomEventConsumer,
					userPermissionsUpdatedAfterAddWatcherEventConsumer,
					userPermissionsUpdatedAfterAddSecondPlayerEventConsumer,
					userPermissionsUpdatedAfterLeftLobbyEventConsumer,
					userPermissionsUpdatedAfterLoggedOutOpenByLeftBeforeGameStartedEventConsumer,
					userPermissionsUpdatedAfterLoggedOutOpenByLeftEventConsumer,
					userPermissionsUpdatedAfterLoggedOutWatcherLeftLastEventConsumer,
					userPermissionsUpdatedAfterLoggedOutWatcherLeftEventConsumer,
					userPermissionsUpdatedAfterLoggedOutOpenByLeftFirstEventConsumer,
					userPermissionsUpdatedAfterLoggedOutSecondLeftFirstEventConsumer,
					userPermissionsUpdatedAfterLoggedOutSecondLeftEventConsumer,
					userPermissionsUpdatedAfterLoggedOutOpenByLeftLastEventConsumer,
					userPermissionsUpdatedAfterLoggedOutSecondLeftLastEventConsumer,
					userPermissionsUpdatedAfterOpenByLeftBeforeGameStartedEventConsumer,
					userPermissionsUpdatedAfterOpenByLeftEventConsumer,
					userPermissionsUpdatedAfterWatcherLeftLastEventConsumer,
					userPermissionsUpdatedAfterWatcherLeftEventConsumer,
					userPermissionsUpdatedAfterOpenByLeftFirstEventConsumer,
					userPermissionsUpdatedAfterSecondLeftFirstEventConsumer,
					userPermissionsUpdatedAfterSecondLeftEventConsumer,
					userPermissionsUpdatedAfterOpenByLeftLastEventConsumer));
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
		
		initSingleProducer(getUsersUpdateViewAckEventProducer, KafkaUtils.GET_USERS_UPDATE_VIEW_ACK_EVENT_TOPIC, usersUpdateViewQueue);
		
		executeProducersAndConsumers(Arrays.asList(userNameAvailabilityCheckedEventProducer, 
				userEmailAvailabilityCheckedEventProducer,
				logInUserAckEventProducer,
				logOutUserAckEventProducer,
				newUserCreatedEventAckProducer,
				loggedInEventAckProducer,
				getUsersUpdateViewAckEventProducer));		
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
