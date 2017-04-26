package org.moshe.arad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
public class UsersView {

//	@Autowired
//	private SetOperations<String, String> setOperations;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public static final String EMAILS_KEY = "emails";
	public static final String USER_NAMES_KEY = "userNames";
	
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
}
