package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.ExistingUserJoinedLobbyEvent;
import org.moshe.arad.kafka.events.NewUserJoinedLobbyEvent;
import org.moshe.arad.services.UsersView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class ExistingUserJoinedLobbyEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private UsersView usersView;
	
	Logger logger = LoggerFactory.getLogger(ExistingUserJoinedLobbyEventConsumer.class);
	
	public ExistingUserJoinedLobbyEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		ExistingUserJoinedLobbyEvent existingUserJoinedLobbyEvent = convertJsonBlobIntoEvent(record.value());
		
		logger.info("Will check if user exists in one of redis sets...");
		
		boolean isUserInCreatedAndLoggedInStatus = usersView.isBackgammonUserExistsInCreatedAndLoggedIn(existingUserJoinedLobbyEvent.getBackgammonUser());
		boolean isUserInGameStatus =  usersView.isBackgammonUserExistsInGame(existingUserJoinedLobbyEvent.getBackgammonUser());
		boolean isUserInLobbyStatus = usersView.isBackgammonUserExistsInLobby(existingUserJoinedLobbyEvent.getBackgammonUser());
		boolean isUserLoggedInStatus = usersView.isBackgammonUserExistsInLoggedIn(existingUserJoinedLobbyEvent.getBackgammonUser());
		boolean isUserInLoggedOutStatus =  usersView.isBackgammonUserExistsInLoggedOut(existingUserJoinedLobbyEvent.getBackgammonUser());
		
		if(isUserInCreatedAndLoggedInStatus || 
				isUserInGameStatus ||
				isUserInLobbyStatus ||
				isUserInLoggedOutStatus ||
				isUserLoggedInStatus){
			logger.info("User found...");
			logger.info("Will place user in logged in set...");
			
			if(isUserInCreatedAndLoggedInStatus) usersView.removeUserFromCreatedAndLoggedIn(existingUserJoinedLobbyEvent.getBackgammonUser());
			if(isUserInGameStatus) usersView.removeUserFromGame(existingUserJoinedLobbyEvent.getBackgammonUser());
			if(isUserInLobbyStatus) usersView.removeUserFromLobby(existingUserJoinedLobbyEvent.getBackgammonUser());
			if(isUserInLoggedOutStatus) usersView.removeUserFromLoggedOut(existingUserJoinedLobbyEvent.getBackgammonUser());
			if(isUserLoggedInStatus) usersView.removeUserFromLoggedIn(existingUserJoinedLobbyEvent.getBackgammonUser());
			
			logger.info("User removed...");
			usersView.addBackgammonUserToInLobby(existingUserJoinedLobbyEvent.getBackgammonUser());
			logger.info("User was placed in logged in set...");
		}
	}
	
	private ExistingUserJoinedLobbyEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, ExistingUserJoinedLobbyEvent.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		
	}

}
