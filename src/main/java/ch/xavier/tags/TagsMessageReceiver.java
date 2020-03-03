package ch.xavier.tags;

import ch.xavier.movies.MoviesCacheManager;
import ch.xavier.tags.messages.AddTagsMessage;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TagsMessageReceiver {

    @Autowired
    private MoviesCacheManager manager;

    private final Gson gson = new Gson();

    @RabbitListener(queues = "#{rabbitConfig.getQueueName()}")
    public void receiveMessage(Message message) {

        MDC.put("correlationId", message.getMessageProperties().getCorrelationId());

        AddTagsMessage addTagsMessage = gson.fromJson(new String(message.getBody()), AddTagsMessage.class);

        log.info("Received message to add tags:{} to movieIds:{}", addTagsMessage.getTags(), addTagsMessage.getMovieIds());
        manager.addTagsToMovies(addTagsMessage.getTags(), addTagsMessage.getMovieIds()).subscribe();

        MDC.remove("correlationId");
    }
}
