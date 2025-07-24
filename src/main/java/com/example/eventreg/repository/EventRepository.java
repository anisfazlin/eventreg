package com.example.eventreg.repository;

import com.example.eventreg.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByEventTypeId(String eventTypeId);
    Page<Event> findAll(Pageable pageable);

}