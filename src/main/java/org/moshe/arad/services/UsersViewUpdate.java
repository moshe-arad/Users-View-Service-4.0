package org.moshe.arad.services;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.moshe.arad.kafka.consumers.events.LoggedInEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UsersViewUpdate {

	private RedisTemplate<String, String> redisTemplate;
	
	public static final String NEED_TO_UPDATE = "NeedToUpdate";
	public static final String ALL = "All";
	public static final String GROUP = "Group";
	public static final String USER = "User";
	
	private Object allLocker = new Object();
	private Object groupLocker = new Object();
	private Object userLocker = new Object();
	
	@Autowired
	private ApplicationContext context;
	
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	
	private Logger logger = LoggerFactory.getLogger(LoggedInEventConsumer.class);
	
	@Autowired
    public UsersViewUpdate(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
	public void markNeedToUpdateAllUsers(UsersViewChanges usersViewChanges){
		
		synchronized(allLocker){
			UsersViewChanges existingUsersViewChanges = getUsersViewChangesFromRedis(NEED_TO_UPDATE + ":" + ALL);
			
			unionViews(usersViewChanges, existingUsersViewChanges);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String usersViewChangesJson = null;
			try {
				usersViewChangesJson = objectMapper.writeValueAsString(usersViewChanges);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if(!redisTemplate.hasKey(ALL)) redisTemplate.opsForValue().set(ALL, "1");
			else redisTemplate.opsForValue().increment(ALL, 1);
			
			redisTemplate.opsForValue().set(NEED_TO_UPDATE + ":" + ALL, usersViewChangesJson);
			allLocker.notifyAll();
		}		
	}	
	
	public  void markNeedToUpdateGroupUsers(UsersViewChanges usersViewChanges, String group){
		synchronized(groupLocker){
			UsersViewChanges existingUsersViewChanges = getUsersViewChangesFromRedis(NEED_TO_UPDATE + ":" + GROUP + ":" + group);
			
			unionViews(usersViewChanges, existingUsersViewChanges);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String usersViewChangesJson = null;
			try {
				usersViewChangesJson = objectMapper.writeValueAsString(usersViewChanges);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if(!redisTemplate.opsForHash().hasKey(GROUP, group)) redisTemplate.opsForHash().put(GROUP, group, "1");
			else redisTemplate.opsForHash().increment(GROUP, group, 1);
			
			redisTemplate.opsForValue().set(NEED_TO_UPDATE + ":" + GROUP + ":" + group, usersViewChangesJson);
			groupLocker.notifyAll();
		}		
	}
	
	public  void markNeedToUpdateSingleUser(UsersViewChanges usersViewChanges, String username){
		
		synchronized(userLocker){
			UsersViewChanges existingUsersViewChanges = getUsersViewChangesFromRedis(NEED_TO_UPDATE + ":" + USER + ":" + username);
			
			unionViews(usersViewChanges, existingUsersViewChanges);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String usersViewChangesJson = null;
			try {
				usersViewChangesJson = objectMapper.writeValueAsString(usersViewChanges);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if(!redisTemplate.opsForHash().hasKey(USER, username)) redisTemplate.opsForHash().put(USER, username, "1");
			else redisTemplate.opsForHash().increment(USER, username, 1);
			
			redisTemplate.opsForValue().set(NEED_TO_UPDATE + ":" + USER + ":" + username, usersViewChangesJson);
			userLocker.notifyAll();
		}
	}
	
	private UsersViewChanges getUsersViewChangesFromRedis(String key){
		if(!redisTemplate.hasKey(key)) return null;
		else{
			String usersViewChangesJson = redisTemplate.opsForValue().get(key);
			ObjectMapper objectMapper = new ObjectMapper();
			UsersViewChanges usersViewChanges = null;
			try {
				usersViewChanges = objectMapper.readValue(usersViewChangesJson, UsersViewChanges.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return usersViewChanges;
		}
	}
	
	private void unionViews(UsersViewChanges usersViewChanges, UsersViewChanges existingUsersViewChanges) {
		if(existingUsersViewChanges != null && usersViewChanges != null){
			usersViewChanges.getNewUsersAdded().addAll(existingUsersViewChanges.getNewUsersAdded());
			usersViewChanges.getUsersLoggedIn().addAll(existingUsersViewChanges.getUsersLoggedIn());
			usersViewChanges.getUsersPermissionsUpdated().addAll(existingUsersViewChanges.getUsersPermissionsUpdated());
		}
	}
	
	public UsersViewChanges getNeedToUpdateAllUsers(){
		synchronized (allLocker) {
			UsersViewChanges result = null;
			ObjectMapper objectMapper = new ObjectMapper();
			String usersViewChangesJson = null;
			
			if(!redisTemplate.hasKey(NEED_TO_UPDATE + ":" + ALL)){
				try {
					allLocker.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				usersViewChangesJson = redisTemplate.opsForValue().get(NEED_TO_UPDATE + ":" + ALL);
				if(usersViewChangesJson != null){
					result = objectMapper.readValue(usersViewChangesJson,UsersViewChanges.class);
					checkInnerCounterOf(ALL, NEED_TO_UPDATE + ":" + ALL);
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result;
		}		
	}
	
	public UsersViewChanges getNeedToUpdateGroupUsers(String group){
		synchronized (groupLocker) {
			UsersViewChanges result = null;
			ObjectMapper objectMapper = new ObjectMapper();
			String usersViewChangesJson = null;
			
			if(!redisTemplate.hasKey(NEED_TO_UPDATE + ":" + GROUP + ":" + group)){
				try {
					groupLocker.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				usersViewChangesJson = redisTemplate.opsForValue().get(NEED_TO_UPDATE + ":" + GROUP + ":" + group);
				if(usersViewChangesJson != null){
					result = objectMapper.readValue(usersViewChangesJson,UsersViewChanges.class);
					hashCheckInnerCounterOf(GROUP, group,  NEED_TO_UPDATE + ":" + GROUP + ":" + group);
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result;
		}
	}
	
	public UsersViewChanges getNeedToUpdateUser(String username){
		synchronized (userLocker) {
			UsersViewChanges result = null;
			ObjectMapper objectMapper = new ObjectMapper();
			String usersViewChangesJson = null;
			
			if(!redisTemplate.hasKey(NEED_TO_UPDATE + ":" + USER + ":" + username)){
				try {
					userLocker.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				usersViewChangesJson = redisTemplate.opsForValue().get(NEED_TO_UPDATE + ":" + USER + ":" + username);
				if(usersViewChangesJson != null){
					result = objectMapper.readValue(usersViewChangesJson,UsersViewChanges.class);
					hashCheckInnerCounterOf(USER, username,  NEED_TO_UPDATE + ":" + USER + ":" + username);
				}					
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result;
		}		
	}
	
	private void checkInnerCounterOf(String key, String needToUpdateKey){
		Integer counter = Integer.valueOf(redisTemplate.opsForValue().get(key));
		counter--;
		if(counter.equals(0)) redisTemplate.delete(needToUpdateKey);
		redisTemplate.opsForValue().set(key, counter.toString());
	}
	
	private void hashCheckInnerCounterOf(String key, String hkey, String needToUpdateKey){
		Integer counter = Integer.valueOf(redisTemplate.opsForHash().get(key, hkey).toString());
		counter--;
		if(counter.equals(0)) redisTemplate.delete(needToUpdateKey);
		redisTemplate.opsForHash().put(key, hkey, counter.toString());
	}
}
