//package org.moshe.arad.services.old;
//
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.moshe.arad.entities.BackgammonUser;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class UsersViewOld1 {
//	
//	@Autowired
//	private RedisTemplate<String, String> redisTemplate;
//	
//	public static final String EMAILS_KEY = "emails";
//	public static final String USER_NAMES_KEY = "userNames";
//	public static final String USERS = "Users";
//	
//	private Logger logger = LoggerFactory.getLogger(UsersViewOld.class);
//	
//	public boolean isEmailAvailable(String email){
//		return !redisTemplate.opsForSet().isMember(EMAILS_KEY, email);
//	}
//	
//	public boolean isUserNameAvailable(String userName){
//		return !redisTemplate.opsForSet().isMember(USER_NAMES_KEY, userName);		
//	}
//	
//	public void addEmail(String email){
//		if(this.isEmailAvailable(email)) redisTemplate.opsForSet().add(EMAILS_KEY, email);		
//	}
//
//	public void addUserName(String userName){
//		if(this.isUserNameAvailable(userName)) redisTemplate.opsForSet().add(USER_NAMES_KEY, userName);		
//	}
//	
//	public void addBackgammonUser(BackgammonUser user){
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			redisTemplate.opsForHash().put(USERS, user.getUserName(), objectMapper.writeValueAsString(user));
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//	}
//}
