package org.moshe.arad.kafka.consumers.commands;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.GetUsersUpdateViewCommand;
import org.moshe.arad.kafka.events.GetUsersUpdateViewAckEvent;
import org.moshe.arad.services.UsersView;
import org.moshe.arad.services.UsersViewChanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class GetUsersUpdateViewCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(GetUsersUpdateViewCommandConsumer.class);
	
	@Autowired
	private UsersView usersView;
	
	@Autowired
	private ApplicationContext context;
	
	public GetUsersUpdateViewCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		GetUsersUpdateViewAckEvent getUsersUpdateViewAckEvent = context.getBean(GetUsersUpdateViewAckEvent.class);
		GetUsersUpdateViewCommand getUsersUpdateViewCommand = convertJsonBlobIntoEvent(record.value());
		UsersViewChanges usersViewChanges = null;
		
		logger.info("Get Lobby Update View Command record recieved, " + record.value());
		
		if(getUsersUpdateViewCommand.isAllLevel()){
			usersViewChanges = usersView.getNeedToUpdateAllUsers();
			
		}
		else if(getUsersUpdateViewCommand.isGroupLevel()){
			usersViewChanges = usersView.getNeedToUpdateGroupUsers(getUsersUpdateViewCommand.getGroup());
			
		}
		else if(getUsersUpdateViewCommand.isUserLevel()){
			long startTime = System.nanoTime();
			usersViewChanges = usersView.getNeedToUpdateUser(getUsersUpdateViewCommand.getUser());
			long endTime = System.nanoTime();

			long duration = (endTime - startTime);
			
			logger.info("**********************************");
			logger.info("**********************************");
			logger.info("***** duration = " + duration + "*************");
			logger.info("**********************************");
			logger.info("**********************************");
			
		}
		
		getUsersUpdateViewAckEvent.setUuid(getUsersUpdateViewCommand.getUuid());
		getUsersUpdateViewAckEvent.setUsersViewChanges(usersViewChanges);
		
    	logger.info("passing get Lobby Update View Ack Event to producer...");
    	consumerToProducerQueue.getEventsQueue().put(getUsersUpdateViewAckEvent);
    	logger.info("Event passed to producer...");		
	}

	public ConsumerToProducerQueue getConsumerToProducerQueue() {
		return consumerToProducerQueue;
	}

	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}
	
	private GetUsersUpdateViewCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, GetUsersUpdateViewCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	