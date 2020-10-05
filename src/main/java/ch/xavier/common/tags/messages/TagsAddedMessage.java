package ch.xavier.common.tags.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * Json:
 {
 "tags": ["tag1", "tag2"],
 "movieIds": [263]
 }
 */
@AllArgsConstructor
@Getter
public final class TagsAddedMessage {
    private final Set<String> tags;
    private final Set<Long> movieIds;
}
