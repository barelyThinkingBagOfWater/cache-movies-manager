package ch.xavier.common.tags;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Tag {

    private final String tagName;
    private final Set<Long> movieIds;

    public Tag(String tagName) {
        this.tagName = tagName;
        this.movieIds = Collections.emptySet();
    }

    public Tag(String tagName, Long movieId) {
        this.tagName = tagName;
        this.movieIds = Set.of(movieId);
    }
}
