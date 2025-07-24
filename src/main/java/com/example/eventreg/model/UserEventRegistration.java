package com.example.eventreg.model;

import com.example.eventreg.dto.AnswerDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "registrations")
public class UserEventRegistration {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Event event;

    public String name;
    public String email;
    public String phoneNumber;

    private List<AnswerDTO> answers; // ← directly use your DTO here

    private LocalDateTime registeredAt;

    public UserEventRegistration(User user, Event event, String name, String email, String phoneNumber, List<AnswerDTO> answers, LocalDateTime registeredAt) {
        this.user = user;
        this.event = event;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.answers = answers;
        this.registeredAt = registeredAt;
    }

    public UserEventRegistration(){
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}
