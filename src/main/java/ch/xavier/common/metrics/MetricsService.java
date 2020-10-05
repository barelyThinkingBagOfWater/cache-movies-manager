package ch.xavier.common.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MetricsService {

    private final MovieCountersFactory movieCountersFactory;
    private final TagCountersFactory tagCountersFactory;
    private final RatingCountersFactory ratingCountersFactory;

    @Autowired
    public MetricsService(MovieCountersFactory movieCountersFactory, TagCountersFactory tagCountersFactory,
                          RatingCountersFactory ratingCountersFactory) {
        this.movieCountersFactory = movieCountersFactory;
        this.tagCountersFactory = tagCountersFactory;
        this.ratingCountersFactory = ratingCountersFactory;
    }

    //TAGS
    public void notifyTagAdded() {
        tagCountersFactory.getTagsAddedCounter().increment();
    }

    public void notifyTagAddedError() {
        tagCountersFactory.getTagsAddedErrorsCounter().increment();
    }

    public void notifyTagImported() {
        tagCountersFactory.getTagsImportedCounter().increment();
    }

    public void notifyTagImportedError() {
        tagCountersFactory.getTagsImportedErrorsCounter().increment();
    }

    public void notifyTagSearched() {
        tagCountersFactory.getTagsSearchedCounter().increment();
    }

    //MOVIES
    public void notifyMovieImported() {
        movieCountersFactory.getMoviesImportedCounter().increment();
    }

    public void notifyMovieImportedError() {
        movieCountersFactory.getMoviesImportedErrorsCounter().increment();
    }

    public void notifyMovieSearched() {
        movieCountersFactory.getMoviesSearchedCounter().increment();
    }

    //RATINGS
    public void notifyRatingImported() {
        ratingCountersFactory.getRatingsImportedCounter().increment();
    }

    public void notifyRatingImportedError() {
        ratingCountersFactory.getRatingsImportedErrorsCounter().increment();
    }

    public void notifyRatingAdded() {
        ratingCountersFactory.getRatingsAddedCounter().increment();
    }

    public void notifyRatingAddedError() {
        ratingCountersFactory.getRatingsAddedErrorsCounter().increment();
    }
}