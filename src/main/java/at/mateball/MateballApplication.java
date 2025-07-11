package at.mateball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MateballApplication {

	public static void main(String[] args) {
		SpringApplication.run(MateballApplication.class, args);
	}

}
