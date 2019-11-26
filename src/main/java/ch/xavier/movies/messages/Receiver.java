package ch.xavier.movies.messages;

import ch.xavier.movies.manager.MoviesCacheManager;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Receiver {

    @Autowired
    private MoviesCacheManager manager;

    private final Gson gson = new Gson();

    @RabbitListener(queues = "#{rabbitConfig.getQueueName()}")
    public void receiveMessage(String message) {
        log.debug("Received message : [{}]", message);

        AddTagsMessage addTagsMessage = gson.fromJson(message, AddTagsMessage.class);

        log.info("Received message to add tags:{} to movieIds:{}", addTagsMessage.getTags(), addTagsMessage.getMovieIds());
        manager.addTagsToMovies(addTagsMessage.getTags(), addTagsMessage.getMovieIds()).subscribe();
    }
}
