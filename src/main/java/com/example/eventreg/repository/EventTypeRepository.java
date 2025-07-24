package com.example.eventreg.repository;

import com.example.eventreg.model.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTypeRepository extends MongoRepository<EventType, String> {
    boolean existsByName(String name);
    List<EventType> findByNameIgnoreCase(String name);
}