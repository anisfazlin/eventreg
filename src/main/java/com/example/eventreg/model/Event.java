package com.example.eventreg.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "events")
public class Event {

    @Id
    private String id;

    private String name;
    private String venue;
    private String start_date;
    private String end_date;

    @DBRef
    private EventType eventType;

    @DBRef
    private List<Question> questions = new ArrayList<>();


    public Event() {}

    public Event(String name, EventType eventType, String venue, String start_date, String end_date) {
        this.name = name;
        this.eventType = eventType;
        this.venue = venue;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getStart_date() { return start_date; }
    public void setStart_date(String start_date) { this.start_date = start_date; }

    public String getEnd_date() { return end_date; }
    public void setEnd_date(String end_date) { this.end_date = end_date; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}
