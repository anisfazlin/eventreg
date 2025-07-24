package com.example.eventreg.repository;

import com.example.eventreg.model.User;
import com.example.eventreg.model.Event;
import com.example.eventreg.model.UserEventRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEventRepository extends MongoRepository<UserEventRegistration, String> {
    List<UserEventRegistration> findByUser(Optional<User> user);
    List<UserEventRegistration> findByEvent(Event event);
}
