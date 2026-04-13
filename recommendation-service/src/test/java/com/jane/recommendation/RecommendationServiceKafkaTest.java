package com.jane.recommendation;

import com.jane.recommendation.repository.CustomerGenreRepository;
import com.jane.recommendation.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;

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

    }

    @Test
    public void movieAddedEvent() {

    }

}
