package com.jane.customer.service;

import com.jane.customer.dto.CustomerDetails;
import com.jane.customer.dto.GenreUpdateRequest;
import com.jane.customer.exception.CustomerNotFoundException;
import com.jane.customer.mapper.CustomerMapper;
import com.jane.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CustomerService(CustomerRepository customerRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.customerRepository = customerRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public CustomerDetails getCustomer(Integer customerId) {
        return this.customerRepository.findById(customerId)
                .map(CustomerMapper::toCustomerDetails)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Transactional
    public void updateCustomerGenre(Integer customerId, GenreUpdateRequest request) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        customer.setFavoriteGenre(request.favoriteGenre());

        applicationEventPublisher.publishEvent(CustomerMapper.toGenreUpdatedEvent(customerId, customer.getFavoriteGenre()));

    }


}
