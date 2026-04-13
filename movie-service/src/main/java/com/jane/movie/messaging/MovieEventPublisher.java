package com.jane.movie.messaging;

import com.jane.netflux.events.MovieAddedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MovieEventPublisher {

    @EventListener
    public void onMovieAdded(MovieAddedEvent movieAddedEvent) {
    }

}
