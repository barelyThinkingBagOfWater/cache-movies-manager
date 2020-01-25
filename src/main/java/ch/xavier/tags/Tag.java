package ch.xavier.tags;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Set;

@Getter
@AllArgsConstructor
@ToString
public class Tag {

    private String tagName;
    private Set<Long> movieIds;

    public Tag(String tagName) {
        this.tagName = tagName;
        this.movieIds = Collections.emptySet();
    }

    public Tag(String tagName, Long movieId) {
        this.tagName = tagName;
        this.movieIds = Set.of(movieId);
    }
}
