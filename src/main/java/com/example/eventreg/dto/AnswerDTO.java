package com.example.eventreg.dto;

public class AnswerDTO {
    private String questionId;
    private String questionText; 
    private String response;

    // Constructors
    public AnswerDTO() {}

    public AnswerDTO(String questionId, String questionText, String response) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.response = response;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
