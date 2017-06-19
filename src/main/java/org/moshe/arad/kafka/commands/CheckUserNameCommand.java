package org.moshe.arad.kafka.commands;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component("CheckUserNameAvailabilityCommand")
public class CheckUserNameCommand implements ICommand {

	private UUID uuid;
	private String userName;

	public CheckUserNameCommand() {
	}
	
	public CheckUserNameCommand(UUID uuid, String userName) {
		this.userName = userName;
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "CheckUserNameAvailabilityCommand [uuid=" + uuid + ", userName=" + userName + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
