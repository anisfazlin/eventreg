package com.example.eventreg.repository;

import com.example.eventreg.model.UserEventRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEventRegistrationRepository extends MongoRepository<UserEventRegistration, String> {
    List<UserEventRegistration> findByEventId(String eventId);
    List<UserEventRegistration> findByUserId(String userId);
}

