package org.moshe.arad.services;

import org.moshe.arad.entities.BackgammonUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UsersView {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public static final String EMAILS_KEY = "emails";
	public static final String USER_NAMES_KEY = "userNames";
	
	public static final String CREATED_AND_LOGGED_IN = "CreatedAndLoggedIn";
	public static final String LOGGED_IN = "LoggedIn";
	public static final String LOBBY = "InLobby";
	public static final String GAME = "InGame";
	public static final String LOGGED_OUT = "LoggedOut";
	
	private Logger logger = LoggerFactory.getLogger(UsersView.class);
	
	public boolean isEmailAvailable(String email){
		return !redisTemplate.opsForSet().isMember(EMAILS_KEY, email);
	}
	
	public boolean isUserNameAvailable(String userName){
		return !redisTemplate.opsForSet().isMember(USER_NAMES_KEY, userName);		
	}
	
	public void addEmail(String email){
		if(this.isEmailAvailable(email)) redisTemplate.opsForSet().add(EMAILS_KEY, email);		
	}

	public void addUserName(String userName){
		if(this.isUserNameAvailable(userName)) redisTemplate.opsForSet().add(USER_NAMES_KEY, userName);		
	}
	
	public void addBackgammonUserToCreatedAndLoggedIn(BackgammonUser user){
		this.addBackgammonUser(user, CREATED_AND_LOGGED_IN);
	}
	
	public void addBackgammonUserToLoggedIn(BackgammonUser user){
		this.addBackgammonUser(user, LOGGED_IN);
	}
	
	public void addBackgammonUserToInLobby(BackgammonUser user){
		this.addBackgammonUser(user, LOBBY);
	}
	
	private void addBackgammonUser(BackgammonUser user, String key){
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			redisTemplate.opsForSet().add(key, objectMapper.writeValueAsString(user));
		} catch (JsonProcessingException e) {
			logger.error("Failed to save user as json into redis DB...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
