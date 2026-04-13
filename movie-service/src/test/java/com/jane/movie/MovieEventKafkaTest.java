package com.jane.movie;

import com.jane.netflux.events.MovieAddedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@AutoConfigureRestTestClient
@Import({
        TestcontainersConfiguration.class,
        MovieEventKafkaTest.TestConsumerConfiguration.class
})
@SpringBootTest(
        properties = {
                "app.import-movies=false",
                "spring.cloud.function.definition=testConsumer",
                "spring.cloud.stream.bindings.testConsumer-in-0.destination=movie-events",
                "spring.cloud.stream.kafka.binder.consumer-properties.key.deserializer=org.apache.kafka.common.serialization.IntegerDeserializer",
                "spring.cloud.stream.kafka.binder.consumer-properties.auto.offset.reset=earliest"
        }
)
public class MovieEventKafkaTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private BlockingQueue<Message<MovieAddedEvent>> queue;

    @Test
    public void movieAddedEvent() {

    }

    @TestConfiguration
    static class TestConsumerConfiguration{

        @Bean
        public BlockingQueue<Message<MovieAddedEvent>> queue(){
            return new LinkedBlockingQueue<>();
        }

        @Bean
        Consumer<Message<MovieAddedEvent>> testConsumer(BlockingQueue<Message<MovieAddedEvent>> queue){
            return queue::add;
        }

    }

}
