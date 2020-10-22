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

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Value("#{'${spring.redis.cluster.hosts}'.split(',')}")
    private List<String> clusterHosts;

    @Value("${spring.redis.host}")
    private String singleHost;

    private static final int DEFAULT_PORT =  6379;


    @Bean
    public ReactiveRedisConnectionFactory factory() {
        if (clusterHosts.get(0).isEmpty()) {
            log.info("Connecting to a single Redis node with url:{}", singleHost);
            return new LettuceConnectionFactory(singleHost, DEFAULT_PORT);
        } else {
            log.info("Connecting to a cluster of Redis nodes with urls:{}", clusterHosts);

            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
            redisClusterConfiguration.setClusterNodes(
                    clusterHosts.stream().map(host -> new RedisNode(host, DEFAULT_PORT))
                    .collect(Collectors.toList()));
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
