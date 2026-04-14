package com.jane.movie.service;


import com.jane.movie.dto.MovieDetails;
import com.jane.movie.exception.MovieNotFoundException;
import com.jane.movie.mapper.MovieMapper;
import com.jane.movie.repository.MovieRepository;
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

    public MovieDetails getMovie(Integer movieId) {
        return this.movieRepository.findById(movieId)
                .map(MovieMapper::toMovieDetails)
                .orElseThrow(() -> new MovieNotFoundException(movieId));

    }


    public MovieDetails saveMovie(MovieDetails movieDetails) {
        var movie = this.movieRepository.save(MovieMapper.toMovie(movieDetails));
        applicationEventPublisher.publishEvent(MovieMapper.toMovieAddedEvent(movie));
        return MovieMapper.toMovieDetails(movie);
    }

}
