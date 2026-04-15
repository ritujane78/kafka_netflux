package com.jane.recommendation.messaging;

import com.jane.netflux.events.CustomerGenreUpdatedEvent;
import com.jane.netflux.events.MovieAddedEvent;
import com.jane.recommendation.service.CustomerService;
import com.jane.recommendation.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    private static final Logger log = LoggerFactory.getLogger(EventConsumerConfig.class);

    @Bean
    public Consumer<CustomerGenreUpdatedEvent> customerGenreUpdatedEventConsumer(CustomerService customerService){
        return withLogging(customerService::updateGenre);
    }

    @Bean
    public Consumer<MovieAddedEvent> movieAddedEventConsumer(MovieService movieService){
        return withLogging(movieService::addMovie);
    }

    private <T> Consumer<T> withLogging(Consumer<T> consumer){
        return t -> {
            log.info("received: {}", consumer);
            consumer.accept(t);
        };
    }


}
