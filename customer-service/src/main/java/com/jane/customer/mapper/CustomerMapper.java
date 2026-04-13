package com.jane.customer.mapper;

import com.jane.customer.dto.CustomerDetails;
import com.jane.customer.entity.Customer;
import com.jane.netflux.events.CustomerGenreUpdatedEvent;

import java.time.Instant;

public class CustomerMapper {

    public static CustomerDetails toCustomerDetails(Customer customer){
        return new CustomerDetails(
                customer.getId(),
                customer.getName(),
                customer.getFavoriteGenre()
        );
    }

    public static CustomerGenreUpdatedEvent toGenreUpdatedEvent(Integer customerId, String favoriteGenre) {
        return new CustomerGenreUpdatedEvent(
                customerId,
                favoriteGenre,
                Instant.now()
        );
    }

}
