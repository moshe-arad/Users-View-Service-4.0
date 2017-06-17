package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.Status;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.LoggedInEvent;
import org.moshe.arad.kafka.events.LoggedInEventAck;
import org.moshe.arad.kafka.events.UserPermissionsUpdatedEvent;
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
public class UserPermissionsUpdatedAfterCreateRoomEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private UsersView usersView;
	
	Logger logger = LoggerFactory.getLogger(UserPermissionsUpdatedAfterCreateRoomEventConsumer.class);
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	@Autowired
	private ApplicationContext context;
	
	public UserPermissionsUpdatedAfterCreateRoomEventConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,String> record) {
		try{	    	
			UserPermissionsUpdatedEvent userPermissionsUpdatedEvent = convertJsonBlobIntoEvent(record.value());
			logger.info("User Permissions Updated Event record recieved, " + userPermissionsUpdatedEvent);	             	                		               
	    	logger.info("Updating users view in redis data store...");
	    	
	    	BackgammonUser user = userPermissionsUpdatedEvent.getBackgammonUser();
	    	usersView.addBackgammonUser(user);
	    	logger.info("Update completed...");
	    	
	    	UsersViewChanges usersViewChanges = context.getBean(UsersViewChanges.class);
	    	usersViewChanges.getUsersPermissionsUpdated().add(user);
	    	
	    	usersView.markNeedToUpdateSingleUser(usersViewChanges, user.getUserName());
		}
		catch(Exception ex){
			logger.error("Failed to save data into redis...");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private UserPermissionsUpdatedEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, UserPermissionsUpdatedEvent.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}
}




	