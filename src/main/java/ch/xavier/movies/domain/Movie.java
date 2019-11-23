package ch.xavier.movies.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Set;

@Getter
@NoArgsConstructor
@ToString
public class Movie implements Serializable {

    @Id
    private Long movieId;
    private String title;
    private String genres;
    private Set<String> tags;
}
