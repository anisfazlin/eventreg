package com.example.eventreg.dto;

import java.util.List;

public class AnswerListDTO {
    private List<AnswerDTO> answers;

    // Constructors
    public AnswerListDTO() {}

    public AnswerListDTO(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    // Getters and Setters
    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}
