package com.weewsa.recipebookv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Recipebookv2Application {

	public static void main(String[] args) {
		SpringApplication.run(Recipebookv2Application.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service
//	) {
//		return args -> {
//			service.start();
////			System.out.println("User token: " + service.register(user).getAccessToken());
//		};
//	}
}
