package com.jane.recommendation.service;

import com.jane.recommendation.dto.MovieRecommendations;
import com.jane.recommendation.dto.RecommendationEvents;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
public class RecommendationStreamService { // RecommendationNotifier

    private final Sinks.Many<MovieRecommendations> recommendationsSink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE);
    private final Flux<MovieRecommendations> recommendationsFlux = recommendationsSink.asFlux();
    private final RecommendationService recommendationService;

    public RecommendationStreamService(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    public Flux<MovieRecommendations> streamRecommendations(Integer customerId){
        return this.recommendationsFlux
                .filter(rec -> Objects.isNull(rec.customerId()) || rec.customerId().equals(customerId));
    }

    @Async
    @EventListener // global
    public void onMovieAdded(RecommendationEvents.NewMovieEvent event){
        var movie = this.recommendationService.findMovie(event.movieId());
        var recommendation = MovieRecommendations.newlyAdded(List.of(movie));
        this.recommendationsSink.emitNext(recommendation, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1)));
    }

    @Async
    @EventListener
    public void onPersonalized(RecommendationEvents.PersonalizedEvent event){
        var movies = this.recommendationService.findPersonalized(event.customerId());
        System.out.println("movies: " + movies);
        var recommendation = MovieRecommendations.personalized(event.customerId(), movies);
        this.recommendationsSink.emitNext(recommendation, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1)));

    }

}

