package com.jane.recommendation.dto;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class RecommendationEvents { // acts as namespace

    public record NewMovieEvent(Integer movieId) {

    }

    public record PersonalizedEvent(Integer customerId){

    }

}
