package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.Status;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.NewUserCreatedEvent;
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
public class NewUserJoinedLobbyEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private UsersView usersView;
	
	Logger logger = LoggerFactory.getLogger(NewUserJoinedLobbyEventConsumer.class);
	
	public NewUserJoinedLobbyEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		NewUserJoinedLobbyEvent newUserJoinedLobbyEvent = convertJsonBlobIntoEvent(record.value());
		
		logger.info("Will try to add user to Lobby set...");
		BackgammonUser user = newUserJoinedLobbyEvent.getBackgammonUser();
    	if(!user.getStatus().equals(Status.InLobby)) user.setStatus(Status.InLobby);
    	usersView.addBackgammonUser(user);
    	logger.info("Update completed...");
		logger.info("Done...");
	}
	
	private NewUserJoinedLobbyEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, NewUserJoinedLobbyEvent.class);
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
