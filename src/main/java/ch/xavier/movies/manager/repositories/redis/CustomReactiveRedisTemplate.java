package ch.xavier.movies.manager.repositories.redis;

import ch.xavier.movies.domain.Movie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CustomReactiveRedisTemplate {

    private static final int REDIS_DEFAULT_PORT = 6379;

    @Bean
//    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(
            @Value("${redis.host}") String host) {
        return new LettuceConnectionFactory(host, REDIS_DEFAULT_PORT);
    }

    @Bean
    public org.springframework.data.redis.core.ReactiveRedisTemplate<Long, Movie> reactiveRedisMoviesTemplate(
            ReactiveRedisConnectionFactory factory) {

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Movie> valueSerializer =
                new Jackson2JsonRedisSerializer<>(Movie.class);
        RedisSerializationContext.RedisSerializationContextBuilder<Long, Movie> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<Long, Movie> context =
                builder.value(valueSerializer).build();

        return new org.springframework.data.redis.core.ReactiveRedisTemplate<>(factory, context);
    }
}
