package com.jane.movie;

import com.jane.movie.dto.MovieDetails;
import com.jane.netflux.events.MovieAddedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;


@EnableTestBinder
@AutoConfigureRestTestClient
@SpringBootTest(properties = "app.import-movies=false")
public class MovieEventBinderTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    public void movieAddedEvent() {

        var json = """
                {"title":"Four Rooms","voteAverage":5.784,"voteCount":2436,"releaseDate":"1995-12-09","revenue":4257354,"runtime":98,"backdropPath":"/f2t4JbUvQIjUF5FstG1zZFAp02N.jpg","budget":4000000,"homepage":"https://www.miramax.com/movie/four-rooms/","overview":"It's Ted the Bellhop's first night on the job...and the hotel's very unusual guests are about to place him in some outrageous predicaments. It seems that this evening's room service is serving up one unbelievable happening after another.","popularity":15.295,"posterPath":"/75aHn1NOYXh4M7L5shoeQ6NGykP.jpg","genres":["Comedy"]}
                """;
        var request = JsonMapper.shared().readValue(json, MovieDetails.class);
        var response = testClient.post()
                .uri("/api/movies")
                .body(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(MovieDetails.class)
                .getResponseBody();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals("Four Rooms", response.title());

        var message = this.outputDestination.receive(1000);
        var event = JsonMapper.shared().readValue(message.getPayload(), MovieAddedEvent.class);
        Assertions.assertEquals(response.id(), message.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        Assertions.assertEquals(response.id(), event.movieId());
        Assertions.assertEquals("Four Rooms", event.title());

    }

}
