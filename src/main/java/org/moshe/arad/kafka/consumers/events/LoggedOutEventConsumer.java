package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.Status;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.LoggedInEvent;
import org.moshe.arad.kafka.events.LoggedInEventAck;
import org.moshe.arad.kafka.events.LoggedOutEvent;
import org.moshe.arad.kafka.events.NewUserCreatedEvent;
import org.moshe.arad.kafka.events.NewUserCreatedEventAck;
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
public class LoggedOutEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private UsersView usersView;
	
	Logger logger = LoggerFactory.getLogger(LoggedOutEventConsumer.class);
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	@Autowired
	private ApplicationContext context;
	
	public LoggedOutEventConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,String> record) {
		try{
			LoggedOutEvent loggedOutEvent = convertJsonBlobIntoEvent(record.value());
			logger.info("Logged In Event record recieved, " + loggedOutEvent.getBackgammonUser());	             	                		               
	    	logger.info("Updating users view in redis data store...");
	    	
	    	BackgammonUser user = loggedOutEvent.getBackgammonUser();
	    	if(!user.getStatus().equals(Status.LoggedOut)) user.setStatus(Status.LoggedOut);
	    	usersView.addBackgammonUser(user);
	    	logger.info("Update completed...");
		}
		catch(Exception ex){
			logger.error("Failed to save data into redis...");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private LoggedOutEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LoggedOutEvent.class);
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




	