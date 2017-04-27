package org.moshe.arad.kafka.consumers.commands;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.moshe.arad.kafka.commands.ICommand;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author moshe-arad
 *
 * @param <T> is the event to consume
 * 
 * important to set properties and topic before usage
 */
@Component
@Scope("prototype")
public abstract class SimpleCommandsConsumer implements Runnable, ISimpleCommandConsumer {

	private Logger logger = LoggerFactory.getLogger(SimpleCommandsConsumer.class);
	private static final int CONSUMERS_NUM = 3;
	
	private Consumer<String, String> consumer;
	private boolean isRunning = true;
	private ScheduledThreadPoolExecutor scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(6);
	private String topic;
	private SimpleConsumerConfig simpleConsumerConfig;
	
	public SimpleCommandsConsumer() {
	}

	private void executeConsumers(int numConsumers){
		for(int i=0; i<numConsumers; i++){
			scheduledExecutor.scheduleAtFixedRate( () -> {					    		
	    		while (isRunning){
	                ConsumerRecords<String, String> records = consumer.poll(100);
	                for (ConsumerRecord<String, String> record : records){
	                	consumerOperations(record);	                	
	                }	              	             
	    		}		       		        
			} , 0, 100, TimeUnit.MILLISECONDS);
		}
	}
	
	public void initConsumer(){
		consumer = new KafkaConsumer<String,String>(simpleConsumerConfig.getProperties());
		consumer.subscribe(Arrays.asList(topic));
	}
	
	public abstract void consumerOperations(ConsumerRecord<String,String> record);
	
	@Override
	public void run() {
		this.executeConsumers(CONSUMERS_NUM);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public ScheduledThreadPoolExecutor getScheduledExecutor() {
		return scheduledExecutor;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public SimpleConsumerConfig getSimpleConsumerConfig() {
		return simpleConsumerConfig;
	}

	public void setSimpleConsumerConfig(SimpleConsumerConfig simpleConsumerConfig) {
		this.simpleConsumerConfig = simpleConsumerConfig;
	}
	
	@Override
	public void closeConsumer(){
		 consumer.close();
	}
}
