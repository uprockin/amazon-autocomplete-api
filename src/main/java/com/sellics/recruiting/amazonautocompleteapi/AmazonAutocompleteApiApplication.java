package com.sellics.recruiting.amazonautocompleteapi;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class AmazonAutocompleteApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmazonAutocompleteApiApplication.class, args);
	}
	
	@Bean (name = "taskExecutor")
    public Executor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AmazonSearchThread-");
        executor.initialize();
        return executor;
    }
	
}
