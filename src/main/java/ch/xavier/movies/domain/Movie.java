package ch.xavier.movies.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Movie implements Serializable {

    @Id
    private Long movieId;
    private String title;
    private String genres;
    private Set<String> tags;

    public void addTag(String tag) {
        this.getTags().add(tag);
    }
}
