package com.example.eventreg.controller;

import com.example.eventreg.model.EventType;
import com.example.eventreg.model.Question;
import com.example.eventreg.repository.EventTypeRepository;
import com.example.eventreg.repository.QuestionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/questions")
public class AdminQuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @GetMapping("/index")
    public String listQuestions(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        List<EventType> eventType = eventTypeRepository.findAll();
        Page<Question> questionPage = questionRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("questionPage", questionPage);
        model.addAttribute("eventType", eventType);
        model.addAttribute("isAdmin", true);
        return "views/admin/question-list";
    }

//    @GetMapping("/create")
//    public String showCreateForm(Model model) {
//        model.addAttribute("question", new Question());
//        model.addAttribute("eventTypes", eventTypeRepository.findAll());
//        return "views/admin/question-create";
//    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam(required = false) String categoryId, Model model) {
        model.addAttribute("isAdmin", true);
        Question question = new Question();

        if (categoryId != null && !categoryId.isEmpty()) {
            EventType category = eventTypeRepository.findById(categoryId)
                    .orElse(null); // or throw if you want strict validation
            question.setEventType(category);
        }

        model.addAttribute("question", question);
        model.addAttribute("eventTypes", eventTypeRepository.findAll());
        model.addAttribute("isAdmin", true);
        return "views/admin/question-create";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + id));
        model.addAttribute("question", question);
        model.addAttribute("eventTypes", eventTypeRepository.findAll());
        model.addAttribute("isAdmin", true);
        return "views/admin/question-create";
    }

    @PostMapping("/save")
    public String saveQuestion(@Valid @ModelAttribute("question") Question question,
                               BindingResult result,
                               @RequestParam(required = false) String eventTypeId,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("eventTypes", eventTypeRepository.findAll());
            return "views/admin/question-create";
        }

        if (eventTypeId != null && !eventTypeId.isEmpty()) {
            EventType selectedType = eventTypeRepository.findById(eventTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid EventType ID"));
            question.setEventType(selectedType);
        }

        questionRepository.save(question);
        return "redirect:/admin/questions/index";
    }

    @PostMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable String id) {
        questionRepository.deleteById(id);
        return "redirect:/admin/questions/index";
    }

    @PostMapping("/api/event-type")
    @ResponseBody
    public ResponseEntity<?> saveEventType(@RequestBody EventType eventType) {
        if (eventTypeRepository.existsByName(eventType.getName())) {
            return ResponseEntity.badRequest().body("Event type with this name already exists");
        }
        EventType savedEventType = eventTypeRepository.save(eventType);
        return ResponseEntity.ok(savedEventType);
    }

    @PostMapping("/api/event-type-with-questions")
    @ResponseBody
    public ResponseEntity<?> saveEventTypeWithQuestions(
            @RequestBody EventTypeWithQuestionsRequest request) {

        // Save event type
        EventType eventType = new EventType(request.getName(), request.getDescription());
        if (eventTypeRepository.existsByName(eventType.getName())) {
            return ResponseEntity.badRequest().body("Event type with this name already exists");
        }
        EventType savedEventType = eventTypeRepository.save(eventType);

        // Save questions if provided
        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            for (String questionText : request.getQuestions()) {
                Question question = new Question(savedEventType, questionText);
                questionRepository.save(question);
            }
        }

        return ResponseEntity.ok(savedEventType);
    }

    // Helper class for event type with questions request
    public static class EventTypeWithQuestionsRequest {
        private String name;
        private String description;
        private List<String> questions;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getQuestions() {
            return questions;
        }

        public void setQuestions(List<String> questions) {
            this.questions = questions;
        }
    }
}


