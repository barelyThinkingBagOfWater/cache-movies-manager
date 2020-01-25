package ch.xavier.movies;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Movie implements Serializable {

    private Long movieId;
    private String title;
    private String genres;
    private Set<String> tags;

    public Movie withNewTag(String tag) {
        if (getTags().contains(tag)) {
            return this;
        } else {
            HashSet<String> tags = new HashSet<>(getTags());
            tags.add(tag);

            return new Movie(getMovieId(), getTitle(), getGenres(), tags);
        }
    }
}
