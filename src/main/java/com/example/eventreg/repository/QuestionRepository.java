package com.example.eventreg.repository;

import com.example.eventreg.model.EventType;
import com.example.eventreg.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>
{
    List<Question> findByEventTypeId(String eventTypeId);
}
