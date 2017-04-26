package org.moshe.arad.kafka.consumers.events;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author moshe-arad
 *
 * @param <T> is the event to consume
 * 
 * important to set properties and topic before usage
 */
public abstract class SimpleEventsConsumer implements Runnable, ISimpleEventConsumer {

	Logger logger = LoggerFactory.getLogger(SimpleEventsConsumer.class);
	private static final int CONSUMERS_NUM = 3;
	
	private Consumer<String, String> consumer;
	private boolean isRunning = true;
	private ScheduledThreadPoolExecutor scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(6);
	private String topic;
	private SimpleConsumerConfig simpleConsumerConfig;
	
	public SimpleEventsConsumer() {
	}

	private void executeConsumers(int numConsumers){
		
		while(scheduledExecutor.getQueue().size() < numConsumers){
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			if(scheduledExecutor.getActiveCount() == numConsumers) continue;
			
			scheduledExecutor.scheduleAtFixedRate( () -> {
				consumer.subscribe(Arrays.asList(topic));
	    		
	    		while (isRunning){
	                ConsumerRecords<String, String> records = consumer.poll(100);
	                for (ConsumerRecord<String, String> record : records){
	                	consumerOperations(record);	                	
	                }	              	             
	    		}
		        consumer.close();
		        
			} , 0, 100, TimeUnit.MILLISECONDS);
		}
	}
	
	public void initConsumer(){
		consumer = new KafkaConsumer<String,String>(simpleConsumerConfig.getProperties());
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
}
