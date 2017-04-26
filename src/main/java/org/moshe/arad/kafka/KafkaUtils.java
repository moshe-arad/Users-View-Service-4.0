package org.moshe.arad.kafka;

public class KafkaUtils {

	public static final String SERVERS = "192.168.1.3:9092,192.168.1.3:9093,192.168.1.3:9094";
	public static final String CREATE_NEW_USER_COMMAND_GROUP = "CreateNewUserCommandGroup";
	public static final String STRING_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
	public static final String STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
	public static final String CREATE_NEW_USER_COMMAND_DESERIALIZER = "org.moshe.arad.kafka.deserializers.CreateNewUserCommandDeserializer";
	public static final String NEW_USER_CREATED_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.NewUserCreatedEventSerializer";
	public static final String COMMANDS_TO_USERS_SERVICE_TOPIC = "Commands-To-Users-Service";
	public static final String NEW_USER_CREATED_EVENT_DESERIALIZER = "org.moshe.arad.kafka.deserializers.NewUserCreatedEventDeserializer";
	public static final String NEW_USER_CREATED_EVENT_TOPIC = "New-User-Created-Event";
	public static final String NEW_USER_JOINED_LOBBY_EVENT_TOPIC = "New-User-Created-Event";
	public static final String NEW_USER_JOINED_LOBBY_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.NewUserCreatedEventSerializer";
	public static final String NEW_USER_CREATED_EVENT_GROUP = "NewUserCreatedEventGroup3";
	public static final String CHECK_USER_NAME_AVAILABILITY_COMMAND_DESERIALIZER = "org.moshe.arad.kafka.deserializers.CheckUserNameAvailabilityCommandDeserializer";
	public static final String CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC = "Check-User-Email-Availability-Command";
	public static final String CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC = "Check-User-Name-Availability-Command";
	public static final String USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC = "User-Name-Availability-Checked-Event";
	public static final String CHECK_USER_NAME_AVAILABILITY_GROUP = "CheckUserNameAvailabilityGroup";
	public static final String USER_NAME_AVAILABILITY_CHECKED_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.UserNameAvailabilityCheckedEventSerializer";
	public static final String CHECK_USER_EMAIL_AVAILABILITY_GROUP = "CheckUserEmailAvailabilityGroup";
	public static final String CHECK_USER_EMAIL_AVAILABILITY_COMMAND_DESERIALIZER = "org.moshe.arad.kafka.deserializers.CheckUserEmailAvailabilityCommandDeserializer";
	public static final String USER_EMAIL_AVAILABILITY_CHECKED_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.UserEmailAvailabilityCheckedEventSerializer";
	public static final String EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC = "Email-Availability-Checked-Event";
}