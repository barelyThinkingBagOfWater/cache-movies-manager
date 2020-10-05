package ch.xavier.common.movies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString //for logging during imports
public class Movie {

    @Id
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
