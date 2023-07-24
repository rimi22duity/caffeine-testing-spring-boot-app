package net.therap.caffeinetesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CaffeineTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaffeineTestingApplication.class, args);
	}

}
