package org.moshe.arad;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.kafka.consumers.NewUserCreatedEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersView {

	private Map<String,BackgammonUser> users = new HashMap<>(1000);
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(UsersView.class);
	
	@Autowired
	private NewUserCreatedEventConsumer newUserCreatedEventConsumer;
	
	public void addBackgammonUserToLobby(BackgammonUser user){
		users.put(user.getUserName(), user);
	}
	
	public void acceptNewEvents(){
		logger.info("Started to accept new events from services...");
		executor.execute(newUserCreatedEventConsumer);
		logger.info("Stopped to accept new events from services...");
	}
	
	public void shutdown(){
		newUserCreatedEventConsumer.setRunning(false);
		newUserCreatedEventConsumer.getScheduledExecutor().shutdown();
		
		this.executor.shutdown();
	}
}
