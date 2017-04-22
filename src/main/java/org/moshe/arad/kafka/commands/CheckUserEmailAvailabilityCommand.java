package org.moshe.arad.kafka.commands;

import org.springframework.stereotype.Component;

@Component("CheckUserEmailAvailabilityCommand")
public class CheckUserEmailAvailabilityCommand implements Commandable {

	private String email;

	public CheckUserEmailAvailabilityCommand() {
	}
	
	public CheckUserEmailAvailabilityCommand(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "CheckUserEmailAvailabilityCommand [email=" + email + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
