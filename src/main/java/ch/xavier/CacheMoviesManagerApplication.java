package ch.xavier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.ListenerContainerConsumerFailedEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.event.EventListener;

import java.net.UnknownHostException;

@SpringBootApplication(exclude = { RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class })
@Slf4j
public class CacheMoviesManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheMoviesManagerApplication.class, args);
    }
}
