package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.Status;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.ExistingUserJoinedLobbyEvent;
import org.moshe.arad.view.utils.UsersView;
import org.moshe.arad.view.utils.UsersViewChanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class ExistingUserJoinedLobbyEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private UsersView usersView;
	
	@Autowired
	private ApplicationContext context;
	
	Logger logger = LoggerFactory.getLogger(ExistingUserJoinedLobbyEventConsumer.class);
	
	public ExistingUserJoinedLobbyEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		ExistingUserJoinedLobbyEvent existingUserJoinedLobbyEvent = convertJsonBlobIntoEvent(record.value());
		logger.info("Will check if user exists in one of redis sets...");
		logger.info("Updating users view in redis data store...");    	
    	BackgammonUser user = existingUserJoinedLobbyEvent.getBackgammonUser();
    	if(!user.getStatus().equals(Status.InLobby)) user.setStatus(Status.InLobby);
    	usersView.addBackgammonUser(user);
    	
//    	UsersViewChanges usersViewChanges = context.getBean(UsersViewChanges.class);
//    	usersViewChanges.getUsersLoggedIn().add(user);
//    	
//    	usersView.markNeedToUpdateSingleUser(usersViewChanges, user.getUserName());
    	logger.info("Update completed...");
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
