package ch.xavier.movies.repositories;

import ch.xavier.common.movies.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Value("${spring.redis.url}")
    private String redisUrl;

    @Value("${spring.redis.nodesNumber}")
    private Integer nodesNumber;

    private static final int DEFAULT_PORT = 6379;
    private static final String ID_TEMPLATE = "{id}";


    @Bean
    public ReactiveRedisConnectionFactory factory() {
        if (nodesNumber == 1) {
            log.info("Connecting to a single Redis node with url:{}", redisUrl);

            return new LettuceConnectionFactory(redisUrl, DEFAULT_PORT);
        } else {
            log.info("Connecting to a cluster of Redis nodes with {} nodes and template url:{}", nodesNumber, redisUrl);

            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
            List<RedisNode> redisNodes = new ArrayList<>();

            for (int i = 0; i < nodesNumber; i++) {
                redisNodes.add(new RedisNode(redisUrl.replace(ID_TEMPLATE, String.valueOf(i)), DEFAULT_PORT));
            }

            redisClusterConfiguration.setClusterNodes(redisNodes);
            return new LettuceConnectionFactory(redisClusterConfiguration);
        }
    }

    @Bean
    public ReactiveRedisTemplate<String, Movie> reactiveRedisTemplate() {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Movie> valueSerializer = new Jackson2JsonRedisSerializer<>(Movie.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Movie> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, Movie> context = builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory(), context);
    }
}
