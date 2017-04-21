package org.moshe.arad.kafka.producers.config;

import java.util.Properties;

import org.moshe.arad.kafka.KafkaUtils;

public abstract class SimpleProducerConfig {

	private Properties properties = new Properties();
	
	public SimpleProducerConfig() {
		properties.put("bootstrap.servers", KafkaUtils.SERVERS);
		properties.put("key.serializer", KafkaUtils.KEY_STRING_SERIALIZER);
	}

	@Override
	public String toString() {
		return "SimpleProducerConfig [properties=" + properties + "]";
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
