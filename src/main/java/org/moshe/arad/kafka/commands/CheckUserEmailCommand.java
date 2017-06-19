package org.moshe.arad.kafka.commands;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component("CheckUserEmailAvailabilityCommand")
public class CheckUserEmailCommand implements ICommand {

	private UUID uuid;
	private String email;

	public CheckUserEmailCommand() {
	}
	
	public CheckUserEmailCommand(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "CheckUserEmailAvailabilityCommand [uuid=" + uuid + ", email=" + email + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
