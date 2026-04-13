package com.jane.recommendation;

import com.jane.recommendation.repository.CustomerGenreRepository;
import com.jane.recommendation.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;

@SpringBootTest
@EnableTestBinder
public class RecommendationServiceBinderTest {

    @Autowired
    private InputDestination inputDestination;

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
