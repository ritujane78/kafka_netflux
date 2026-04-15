package com.jane.recommendation;

import com.jane.netflux.events.CustomerGenreUpdatedEvent;
import com.jane.recommendation.dto.MovieRecommendations;
import com.jane.recommendation.dto.RecommendationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;


import java.time.Instant;
import java.util.List;

@SpringBootTest
@EnableTestBinder
@AutoConfigureRestTestClient
@Sql(value = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class RecommendationServiceApiTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private InputDestination inputDestination;

    @Test
    public void recommendations() {
        var response = this.testClient.get()
                .uri("/api/recommendations/{id}",1)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(new ParameterizedTypeReference<List<MovieRecommendations>>() {
                }).getResponseBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(RecommendationType.NEWLY_ADDED, response.getFirst().type());
        Assertions.assertEquals(9, response.getFirst().movies().size());

        Assertions.assertEquals(RecommendationType.PERSONALIZED, response.getLast().type());
        Assertions.assertEquals(4, response.getLast().movies().size());
    }

    @Test
    public void recommendationsStream() throws InterruptedException {
        var event = new CustomerGenreUpdatedEvent(2, "Sci-Fi", Instant.now());

        this.inputDestination.send(MessageBuilder.withPayload(event).build(), "customer-events");

        Thread.sleep(100);

        var response = this.testClient.get()
                .uri("/api/recommendations/2/stream")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(String.class)
                .getResponseBody();

        System.out.println("response: " + response);

        Assertions.assertNotNull(response);

        var recommendation = JsonMapper.shared().readValue(response.substring(5), MovieRecommendations.class);
        Assertions.assertEquals(RecommendationType.PERSONALIZED, recommendation.type());
        Assertions.assertEquals(2, recommendation.customerId());
        Assertions.assertEquals(4, recommendation.movies().size());

    }

}
