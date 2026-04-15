package com.jane.recommendation.service;

import com.jane.netflux.events.MovieAddedEvent;
import com.jane.recommendation.dto.RecommendationEvents;
import com.jane.recommendation.mapper.RecommendationMapper;
import com.jane.recommendation.repository.MovieRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MovieService(MovieRepository movieRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.movieRepository = movieRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void addMovie(MovieAddedEvent movieAddedEvent){
        var movie = RecommendationMapper.toMovie(movieAddedEvent);
        movieRepository.save(movie);
        applicationEventPublisher.publishEvent(new RecommendationEvents.NewMovieEvent(movie.getId()));

    }

}
