package org.moshe.arad.kafka.deserializers;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;

public class CheckUserNameAvailabilityCommandDeserializer implements Deserializer<CheckUserNameAvailabilityCommand> {

	private String encoding = "UTF8";
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CheckUserNameAvailabilityCommand deserialize(String arg0, byte[] data) {
		try {
	        if (data == null){
	            System.out.println("Null recieved at deserialize");
	            return null;
	        }
	        
	        ByteBuffer buf = ByteBuffer.wrap(data);         
	     
	        String userName = deserializeString(buf);
	        UUID uuid = new UUID(buf.getLong(), buf.getLong());
	        
	        CheckUserNameAvailabilityCommand checkUserNameAvailabilityCommand = new CheckUserNameAvailabilityCommand();
	        checkUserNameAvailabilityCommand.setUserName(userName);
	        checkUserNameAvailabilityCommand.setUuid(uuid);	    	      	     
	        return checkUserNameAvailabilityCommand;
	        	            		                      
	    } catch (Exception e) {
	        throw new SerializationException("Error when deserializing byte[] to CreateNewUserCommand");
	    }
	}
	

	private String deserializeString(ByteBuffer buf) throws UnsupportedEncodingException {
		int sizeOfStringDeserialize = buf.getInt();
		byte[] nameBytes = new byte[sizeOfStringDeserialize];
		buf.get(nameBytes);
		String deserializedString = new String(nameBytes, encoding);
		return deserializedString;
	}

}
