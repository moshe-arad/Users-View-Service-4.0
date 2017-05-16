package org.moshe.arad.services;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	
	public void addBackgammonUserToLoggedOut(BackgammonUser user) {
		this.addBackgammonUser(user, LOGGED_OUT);
	}
	
	public boolean isBackgammonUserExistsInCreatedAndLoggedIn(BackgammonUser user){
		return isBackgammonUserExistsInHash(user, CREATED_AND_LOGGED_IN);
	}
	
	public boolean isBackgammonUserExistsInLoggedIn(BackgammonUser user){
		return isBackgammonUserExistsInHash(user, LOGGED_IN);
	}
	
	public boolean isBackgammonUserExistsInLobby(BackgammonUser user){
		return isBackgammonUserExistsInHash(user, LOBBY);
	}
	
	public boolean isBackgammonUserExistsInGame(BackgammonUser user){
		return isBackgammonUserExistsInHash(user, GAME);
	}
	
	public boolean isBackgammonUserExistsInLoggedOut(BackgammonUser user){
		return isBackgammonUserExistsInHash(user, LOGGED_OUT);
	}
	
	public void removeUserFromCreatedAndLoggedIn(BackgammonUser user){
		if(isBackgammonUserExistsInCreatedAndLoggedIn(user)){
			removeUserFrom(user, CREATED_AND_LOGGED_IN);
		}
	}
	
	public void removeUserFromLoggedIn(BackgammonUser user){
		if(isBackgammonUserExistsInLoggedIn(user)){
			removeUserFrom(user, LOGGED_IN);
		}
	}
	
	public void removeUserFromLobby(BackgammonUser user){
		if(isBackgammonUserExistsInLobby(user)){
			removeUserFrom(user, LOBBY);
		}
	}
	
	public void removeUserFromGame(BackgammonUser user){
		if(isBackgammonUserExistsInGame(user)){
			removeUserFrom(user, GAME);
		}
	}
	
	public void removeUserFromLoggedOut(BackgammonUser user){
		if(isBackgammonUserExistsInLoggedOut(user)){
			removeUserFrom(user, LOGGED_OUT);
		}
	}
	
	public BackgammonUser getBackgammonUser(BackgammonUser user){
		if(isBackgammonUserExistsInCreatedAndLoggedIn(user)){
			return getBackgammonUser(user, CREATED_AND_LOGGED_IN);
		}
		else if(isBackgammonUserExistsInGame(user)){
			return getBackgammonUser(user, GAME);
		}
		else if(isBackgammonUserExistsInLobby(user)){
			return getBackgammonUser(user, LOBBY);
		}
		else if(isBackgammonUserExistsInLoggedIn(user)){
			return getBackgammonUser(user, LOGGED_IN);
		}
		else if(isBackgammonUserExistsInLoggedOut(user)){
			return getBackgammonUser(user, LOGGED_OUT);
		}
		else return null;
	}
	
	private BackgammonUser getBackgammonUser(BackgammonUser user, String key){
		String jsonUser = (String) redisTemplate.opsForHash().get(key, user.getUserName());
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonUser, BackgammonUser.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void removeUserFrom(BackgammonUser user, String key){
		if(isBackgammonUserExistsInHash(user, key)){
			redisTemplate.opsForHash().delete(key, user.getUserName());
		}
	}
	
	private boolean isBackgammonUserExistsInHash(BackgammonUser user, String key){
		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<Object, Object> users = redisTemplate.opsForHash().entries(key);
		Iterator<Entry<Object, Object>> it = users.entrySet().iterator();
		while(it.hasNext()){
			if(user.getUserName().equals(it.next().getKey())) return true;		
		}
		
		return false;
	}
	
	private void addBackgammonUser(BackgammonUser user, String key){
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			redisTemplate.opsForHash().put(key, user.getUserName(),objectMapper.writeValueAsString(user));
		} catch (JsonProcessingException e) {
			logger.error("Failed to save user as json into redis DB...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}	
}
