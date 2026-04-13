package com.jane.customer;

import com.jane.customer.dto.GenreUpdateRequest;
import com.jane.netflux.events.CustomerGenreUpdatedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@EnableTestBinder
@AutoConfigureRestTestClient
public class CustomerEventBinderTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerEventBinderTest.class);
    @Autowired
    private RestTestClient testClient;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    public void genreUpdatedEvent() {
        var request = new GenreUpdateRequest("Thriller");
        testClient.patch()
                .uri("/api/customers/{id}/genre", 1)
                .body(request)
                .exchange()
                .expectStatus().isNoContent();

        var message = outputDestination.receive(1000);
        logger.info("message={}", message);
        var event = JsonMapper.shared().readValue(message.getPayload(), CustomerGenreUpdatedEvent.class);

        Assertions.assertEquals(1, message.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        Assertions.assertEquals(1, event.customerId());
        Assertions.assertEquals("Thriller", event.favoriteGenre());
    }

}
