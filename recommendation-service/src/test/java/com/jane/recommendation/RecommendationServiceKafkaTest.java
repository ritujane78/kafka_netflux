package com.jane.recommendation;

import com.jane.netflux.events.CustomerGenreUpdatedEvent;
import com.jane.netflux.events.MovieAddedEvent;
import com.jane.recommendation.repository.CustomerGenreRepository;
import com.jane.recommendation.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.shaded.org.awaitility.core.ThrowingRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class RecommendationServiceKafkaTest {

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private CustomerGenreRepository customerGenreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void customerGenreUpdatedEvent() {
        var event = new CustomerGenreUpdatedEvent(1, "Action", Instant.now());
        this.streamBridge.send( "customer-events", event);
        this.waitUntilAsserted(() -> {
            var customerGenreEvent = this.customerGenreRepository.findById(1).orElseThrow(Assertions::fail);
            Assertions.assertEquals("Action", customerGenreEvent.getFavoriteGenre());
        });
    }

    @Test
    public void movieAddedEvent() {
        var event = new MovieAddedEvent(1, "Inception", 100, 10,null, List.of("Action"), null, Instant.now());
        this.streamBridge.send( "movie-events", event);
        this.waitUntilAsserted(() -> {
            var movie = this.movieRepository.findById(1).orElseThrow(Assertions::fail);
            Assertions.assertEquals("Inception", movie.getTitle());
        });
    }

    private void waitUntilAsserted(ThrowingRunnable runnable) {
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(runnable);
    }

}
