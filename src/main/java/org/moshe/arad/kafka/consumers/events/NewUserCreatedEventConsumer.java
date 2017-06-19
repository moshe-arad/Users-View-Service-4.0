package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.NewUserCreatedEvent;
import org.moshe.arad.kafka.events.NewUserCreatedEventAck;
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
public class NewUserCreatedEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private UsersView usersView;
	
	Logger logger = LoggerFactory.getLogger(NewUserCreatedEventConsumer.class);
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	@Autowired
	private ApplicationContext context;
	
	public NewUserCreatedEventConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,String> record) {
		try{
			NewUserCreatedEvent newUserCreatedEvent = convertJsonBlobIntoEvent(record.value());
			logger.info("New User Created Event record recieved, " + newUserCreatedEvent.getBackgammonUser());	             	                		               
	    	logger.info("Updating user names in redis data store...");
	    	usersView.addUserName(newUserCreatedEvent.getBackgammonUser().getUserName());
	    	logger.info("Update completed...");
	    	logger.info("Updating emails in redis data store...");
	    	usersView.addEmail(newUserCreatedEvent.getBackgammonUser().getEmail());
	    	logger.info("Updating created and logged in users set redis data store...");
	    	usersView.addBackgammonUser(newUserCreatedEvent.getBackgammonUser());
	    	
	    	UsersViewChanges usersViewChanges = context.getBean(UsersViewChanges.class);
	    	usersViewChanges.getUsersLoggedIn().add(newUserCreatedEvent.getBackgammonUser());
	    	
	    	usersView.markNeedToUpdateSingleUser(usersViewChanges, newUserCreatedEvent.getBackgammonUser().getUserName());
	    	
	    	logger.info("Passing ack to next service (lobby service)...");
	    	NewUserCreatedEventAck newUserCreatedEventAck = context.getBean(NewUserCreatedEventAck.class);
	    	newUserCreatedEventAck.setUuid(newUserCreatedEvent.getUuid());
	    	newUserCreatedEventAck.setArrived(newUserCreatedEvent.getArrived());
	    	newUserCreatedEvent.setClazz("newUserCreatedEventAck");
	    	newUserCreatedEventAck.setBackgammonUser(newUserCreatedEvent.getBackgammonUser());
	    	
	    	consumerToProducerQueue.getEventsQueue().put(newUserCreatedEventAck);
	    	logger.info("Update completed...");
		}
		catch(Exception ex){
			logger.error("Failed to save data into redis...");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private NewUserCreatedEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, NewUserCreatedEvent.class);
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




	