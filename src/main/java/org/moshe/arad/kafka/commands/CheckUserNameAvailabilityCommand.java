package org.moshe.arad.kafka.commands;

import org.springframework.stereotype.Component;

@Component("CheckUserNameAvailabilityCommand")
public class CheckUserNameAvailabilityCommand implements Commandable {

	private String userName;

	public CheckUserNameAvailabilityCommand(String userName) {
		this.userName = userName;
	}
	
	@Override
	public String toString() {
		return "CheckUserNameAvailabilityCommand [userName =" + userName + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
