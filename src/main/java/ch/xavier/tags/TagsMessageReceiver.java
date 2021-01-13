package ch.xavier.tags;

import ch.xavier.common.tags.messages.TagsAddedMessage;
import ch.xavier.movies.MoviesCacheManager;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class TagsMessageReceiver {

    @Autowired
    private MoviesCacheManager manager;

    private final Gson gson = new Gson();

    @RabbitListener(queues = "#{rabbitConfig.getQueueName()}")
    public void receiveMessage(Message message) {
        MDC.put("correlationId", message.getMessageProperties().getCorrelationId());

        TagsAddedMessage tagsAddedMessage = gson.fromJson(new String(message.getBody(), StandardCharsets.UTF_8),
                TagsAddedMessage.class);

        log.info("Received message to add tags:{} to movieIds:{}", tagsAddedMessage.getTags(), tagsAddedMessage.getMovieIds());
        manager.addTagsToMovies(tagsAddedMessage.getTags(), tagsAddedMessage.getMovieIds()).subscribe();

        MDC.remove("correlationId");
    }
}
