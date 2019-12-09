package ch.xavier.movies.cache.manager.repositories.redis;

import ch.xavier.movies.cache.domain.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Autowired
    private ReactiveRedisConnectionFactory factory;

    @Bean
    public ReactiveRedisTemplate<String, Movie> reactiveRedisTemplate() {
//        return new ReactiveRedisTemplate<String, Movie>(factory,
//                RedisSerializationContext.fromSerializer(new Jackson2JsonRedisSerializer(Movie.class)));

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Movie> valueSerializer = new Jackson2JsonRedisSerializer<>(Movie.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Movie> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, Movie> context =
                builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
