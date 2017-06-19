package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.Status;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.LoggedInEvent;
import org.moshe.arad.kafka.events.LoggedInEventAck;
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
public class LoggedInEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private UsersView usersView;
	
	Logger logger = LoggerFactory.getLogger(LoggedInEventConsumer.class);
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	@Autowired
	private ApplicationContext context;
	
	public LoggedInEventConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,String> record) {
		try{	    	
			LoggedInEvent loggedInEvent = convertJsonBlobIntoEvent(record.value());
			logger.info("Logged In Event record recieved, " + loggedInEvent.getBackgammonUser());	             	                		               
	    	logger.info("Updating users view in redis data store...");
	    	
	    	BackgammonUser user = loggedInEvent.getBackgammonUser();
	    	if(!user.getStatus().equals(Status.LoggedIn)) user.setStatus(Status.LoggedIn);
	    	usersView.addBackgammonUser(user);
	    	logger.info("Update completed...");
	    	
	    	UsersViewChanges usersViewChanges = context.getBean(UsersViewChanges.class);
	    	usersViewChanges.getUsersLoggedIn().add(user);
	    	
	    	usersView.markNeedToUpdateSingleUser(usersViewChanges, user.getUserName());
	    	
	    	LoggedInEventAck loggedInEventAck = context.getBean(LoggedInEventAck.class);
	    	loggedInEventAck.setUuid(loggedInEvent.getUuid());
	    	loggedInEventAck.setBackgammonUser(loggedInEvent.getBackgammonUser());
	    	
	    	consumerToProducerQueue.getEventsQueue().put(loggedInEventAck);
	    	logger.info("Update completed...");
		}
		catch(Exception ex){
			logger.error("Failed to save data into redis...");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private LoggedInEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LoggedInEvent.class);
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




	