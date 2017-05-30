package org.moshe.arad.services;

import org.moshe.arad.entities.BackgammonUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersView {

	@Autowired
	private UsersViewUpdate usersViewUpdate;
	
	@Autowired
	private UsersViewSimple usersViewSimple;
	
	public void markNeedToUpdateAllUsers(UsersViewChanges usersViewChanges){
		usersViewUpdate.markNeedToUpdateAllUsers(usersViewChanges);
	}	
	
	public  void markNeedToUpdateGroupUsers(UsersViewChanges usersViewChanges, String group){
		usersViewUpdate.markNeedToUpdateGroupUsers(usersViewChanges, group);		
	}
	
	public  void markNeedToUpdateSingleUser(UsersViewChanges usersViewChanges, String username){
		usersViewUpdate.markNeedToUpdateSingleUser(usersViewChanges, username);
	}
	
	public UsersViewChanges getNeedToUpdateAllUsers(){
		return usersViewUpdate.getNeedToUpdateAllUsers();
	}
	
	public UsersViewChanges getNeedToUpdateGroupUsers(String group){
		return usersViewUpdate.getNeedToUpdateGroupUsers(group);
	}
	
	public UsersViewChanges getNeedToUpdateUser(String username){
		return usersViewUpdate.getNeedToUpdateUser(username);		
	}
	
	public boolean isEmailAvailable(String email){
		return usersViewSimple.isEmailAvailable(email);
	}
	
	public boolean isUserNameAvailable(String userName){
		return usersViewSimple.isUserNameAvailable(userName);
	}
	
	public void addEmail(String email){
		usersViewSimple.addEmail(email);		
	}

	public void addUserName(String userName){
		usersViewSimple.addUserName(userName);		
	}
	
	public void addBackgammonUser(BackgammonUser user){
		usersViewSimple.addBackgammonUser(user);
	}
}
