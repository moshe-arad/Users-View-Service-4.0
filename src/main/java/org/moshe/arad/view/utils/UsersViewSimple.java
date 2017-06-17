package org.moshe.arad.view.utils;

import org.moshe.arad.entities.BackgammonUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UsersViewSimple {

	private RedisTemplate<String, String> redisTemplate;
	
	public static final String EMAILS_KEY = "emails";
	public static final String USER_NAMES_KEY = "userNames";
	public static final String USERS = "Users";
	
	private Logger logger = LoggerFactory.getLogger(UsersViewSimple.class);
	
	@Autowired
    public UsersViewSimple(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
	public boolean isEmailAvailable(String email){
		if(redisTemplate.hasKey(EMAILS_KEY)) return !redisTemplate.opsForSet().isMember(EMAILS_KEY, email);
		else return true;
	}
	
	public boolean isUserNameAvailable(String userName){
		if(redisTemplate.hasKey(USER_NAMES_KEY)) return !redisTemplate.opsForSet().isMember(USER_NAMES_KEY, userName);
		else return true;
	}
	
	public void addEmail(String email){
		if(this.isEmailAvailable(email)) redisTemplate.opsForSet().add(EMAILS_KEY, email);		
	}

	public void addUserName(String userName){
		if(this.isUserNameAvailable(userName)) redisTemplate.opsForSet().add(USER_NAMES_KEY, userName);		
	}
	
	public void addBackgammonUser(BackgammonUser user){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			redisTemplate.opsForHash().put(USERS, user.getUserName(), objectMapper.writeValueAsString(user));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
