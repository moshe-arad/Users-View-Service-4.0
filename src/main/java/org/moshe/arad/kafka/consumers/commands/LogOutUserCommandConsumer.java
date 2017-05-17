//package org.moshe.arad.kafka.consumers.commands;
//
//import java.io.IOException;
//import java.util.Date;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.moshe.arad.entities.BackgammonUser;
//import org.moshe.arad.entities.Status;
//import org.moshe.arad.kafka.ConsumerToProducerQueue;
//import org.moshe.arad.kafka.commands.LogInUserCommand;
//import org.moshe.arad.kafka.commands.LogOutUserCommand;
//import org.moshe.arad.kafka.events.LogInUserAckEvent;
//import org.moshe.arad.kafka.events.LogOutUserAckEvent;
//import org.moshe.arad.services.UsersView;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//@Scope("prototype")
//public class LogOutUserCommandConsumer extends SimpleCommandsConsumer {
//
//	private ConsumerToProducerQueue consumerToProducerQueue;
//	private Logger logger = LoggerFactory.getLogger(LogOutUserCommandConsumer.class);
//	
//	@Autowired
//	private UsersView usersView;
//	
//	@Autowired
//	private ApplicationContext context;
//	
//	public LogOutUserCommandConsumer() {
//	}
//
//	@Override
//	public void consumerOperations(ConsumerRecord<String, String> record) {
//		LogOutUserAckEvent logOutUserAckEvent = context.getBean(LogOutUserAckEvent.class);
//		
//		LogOutUserCommand logOutUserCommand = convertJsonBlobIntoEvent(record.value());
//		
//		BackgammonUser user = logOutUserCommand.getBackgammonUser();
//    	if(!user.getStatus().equals(Status.LoggedOut)) user.setStatus(Status.LoggedOut);
//    	usersView.addBackgammonUser(user);
//    	logger.info("Update completed...");
//   
//		logOutUserAckEvent.setBackgammonUser(user);
//		logOutUserAckEvent.setUserFound(true);
//		logOutUserAckEvent.setUuid(logOutUserCommand.getUuid());
//		logOutUserAckEvent.setArrived(new Date());
//		logOutUserAckEvent.setClazz("LogOutUserAckEvent");
//		
//		consumerToProducerQueue.getEventsQueue().put(logOutUserAckEvent);
//		
//		logger.info("Log Out User Ack Event was sent to producer...");
//	}
//	
//	public ConsumerToProducerQueue getConsumerToProducerQueue() {
//		return consumerToProducerQueue;
//	}
//
//	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
//		this.consumerToProducerQueue = consumerToProducerQueue;
//	}
//
//	private LogOutUserCommand convertJsonBlobIntoEvent(String JsonBlob){
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			return objectMapper.readValue(JsonBlob, LogOutUserCommand.class);
//		} catch (IOException e) {
//			logger.error("Falied to convert Json blob into Event...");
//			logger.error(e.getMessage());
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//}
