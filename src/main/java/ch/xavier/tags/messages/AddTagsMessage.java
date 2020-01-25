package ch.xavier.tags.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

/**
 * Json:
 * {
 * "tags": ["tag1", "tag2"],
 * "movieIds": [263]
 * }
 */
@AllArgsConstructor
@Getter
@ToString
public class AddTagsMessage {
    private Set<String> tags;
    private Set<Long> movieIds;
}
