package org.moshe.arad.kafka.serializers;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;

public class UserEmailAvailabilityCheckedEventSerializer implements Serializer<UserEmailAvailabilityCheckedEvent>{

	private static final String encoding = "UTF8";
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String arg0, UserEmailAvailabilityCheckedEvent event) {
		byte[] serializedIsAvailable = new byte[1];
		long highUuid;
		long lowUuid;
		
		 try {
			 if (event == null)
				 return null;
            
			 serializedIsAvailable[0] = ((byte)(event.isAvailable() ? 1 : 0));	
			 highUuid = event.getUuid().getMostSignificantBits();
			 lowUuid = event.getUuid().getLeastSignificantBits();
			 
			 ByteBuffer buf = ByteBuffer.allocate(1+8+8);
			 buf.put(serializedIsAvailable);
			 buf.putLong(highUuid);
			 buf.putLong(lowUuid);
			 
	         return buf.array();
	        } catch (Exception e) {
	            throw new SerializationException("Error when serializing UserEmailAvailabilityCheckedEvent to byte[]");
	        }
	}

}
