package com.example.userservice;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableKafka
public class UserserviceApplication {
	private static final Logger logger = LoggerFactory.getLogger(UserserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		try (AdminClient admin = AdminClient.create(props)) {
			DescribeClusterResult result = admin.describeCluster();
			String clusterId = result.clusterId().get();
			logger.info("Connected to Kafka cluster with ID: {}", clusterId);
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Failed to get Kafka cluster ID", e);
		}
	}
}