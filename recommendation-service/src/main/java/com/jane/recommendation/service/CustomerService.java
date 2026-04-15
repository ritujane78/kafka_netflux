package com.jane.recommendation.service;

import com.jane.netflux.events.CustomerGenreUpdatedEvent;
import com.jane.recommendation.dto.RecommendationEvents;
import com.jane.recommendation.entity.CustomerGenre;
import com.jane.recommendation.mapper.RecommendationMapper;
import com.jane.recommendation.repository.CustomerGenreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerGenreRepository customerGenreRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CustomerService(CustomerGenreRepository customerGenreRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.customerGenreRepository = customerGenreRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void updateGenre(CustomerGenreUpdatedEvent genreUpdatedEvent){
        var entity = RecommendationMapper.toCustomerGenre(genreUpdatedEvent);
        customerGenreRepository.save(entity);
        applicationEventPublisher.publishEvent(new RecommendationEvents.PersonalizedEvent(entity.getCustomerId()));

    }

}
