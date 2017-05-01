package org.moshe.arad.kafka.consumers.commands;

import java.io.IOException;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.LogInUserCommand;
import org.moshe.arad.kafka.events.LogInUserAckEvent;
import org.moshe.arad.services.UsersView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class LogInUserCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(LogInUserCommandConsumer.class);
	
	@Autowired
	private UsersView usersView;
	
	@Autowired
	private ApplicationContext context;
	
	public LogInUserCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LogInUserAckEvent logInUserAckEvent = context.getBean(LogInUserAckEvent.class);
		
		LogInUserCommand logInUserCommand = convertJsonBlobIntoEvent(record.value());
		
		logger.info("Will check if user exists in one of redis sets...");
		
		boolean isUserInCreatedAndLoggedInStatus = usersView.isBackgammonUserExistsInCreatedAndLoggedIn(logInUserCommand.getUser());
		boolean isUserInGameStatus =  usersView.isBackgammonUserExistsInGame(logInUserCommand.getUser());
		boolean isUserInLobbyStatus = usersView.isBackgammonUserExistsInLobby(logInUserCommand.getUser());
		boolean isUserLoggedInStatus = usersView.isBackgammonUserExistsInLoggedIn(logInUserCommand.getUser());
		boolean isUserInLoggedOutStatus =  usersView.isBackgammonUserExistsInLoggedOut(logInUserCommand.getUser());
		
		if(isUserInCreatedAndLoggedInStatus || 
				isUserInGameStatus ||
				isUserInLobbyStatus ||
				isUserInLoggedOutStatus ||
				isUserLoggedInStatus){
			logger.info("User found...");
			logger.info("Will place user in logged in set...");
			
			if(isUserInCreatedAndLoggedInStatus) usersView.removeUserFromCreatedAndLoggedIn(logInUserCommand.getUser());
			if(isUserInGameStatus) usersView.removeUserFromGame(logInUserCommand.getUser());
			if(isUserInLobbyStatus) usersView.removeUserFromLobby(logInUserCommand.getUser());
			if(isUserInLoggedOutStatus) usersView.removeUserFromLoggedOut(logInUserCommand.getUser());
			if(isUserLoggedInStatus) usersView.removeUserFromCreatedAndLoggedIn(logInUserCommand.getUser());
			
			logger.info("User removed...");
			usersView.addBackgammonUserToLoggedIn(logInUserCommand.getUser());
			logger.info("User was placed in logged in set...");
			
			logInUserAckEvent.setUserFound(true);
			logInUserAckEvent.setBackgammonUser(usersView.getBackgammonUser(logInUserAckEvent.getBackgammonUser()));
		}
		else logInUserAckEvent.setUserFound(false);
		
		logInUserAckEvent.setUuid(logInUserCommand.getUuid());
		logInUserAckEvent.setArrived(new Date());
		logInUserAckEvent.setClazz("LogInUserAckEvent");
		
		consumerToProducerQueue.getEventsQueue().put(logInUserAckEvent);
		
		logger.info("Log In User Ack Event was sent to producer...");
	}
	
	public ConsumerToProducerQueue getConsumerToProducerQueue() {
		return consumerToProducerQueue;
	}

	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}

	private LogInUserCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LogInUserCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
}
