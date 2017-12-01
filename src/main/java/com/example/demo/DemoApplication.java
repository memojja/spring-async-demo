package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class DemoApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



	private final GitHubLookupService gitHubLookupService;

	@Autowired
	public DemoApplication(GitHubLookupService gitHubLookupService) {
		this.gitHubLookupService = gitHubLookupService;
	}

	//for customize
	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("memojja-");
		executor.initialize();
		return executor;
	}


	@Override
	public void run(String... strings) throws Exception {

		// Kick of multiple, asynchronous lookups
		CompletableFuture<User> page0 = gitHubLookupService.findUser("memojja");
		CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
		CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
		CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");

		// Wait until they are all done
		CompletableFuture.allOf(page1,page2,page3).join();

	}
}
