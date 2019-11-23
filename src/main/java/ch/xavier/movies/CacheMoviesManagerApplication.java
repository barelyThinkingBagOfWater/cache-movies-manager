package ch.xavier.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = { RedisAutoConfiguration.class }) //I provide my own
public class CacheMoviesManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheMoviesManagerApplication.class, args);
	}

}
