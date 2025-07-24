package com.example.eventreg.controller;

import com.example.eventreg.model.Event;
import com.example.eventreg.model.EventType;
import com.example.eventreg.model.Question;
import com.example.eventreg.model.UserEventRegistration;
import com.example.eventreg.repository.EventRepository;
import com.example.eventreg.repository.EventTypeRepository;
import com.example.eventreg.repository.QuestionRepository;
import com.example.eventreg.repository.UserEventRegistrationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/events")
public class AdminEventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private UserEventRegistrationRepository registrationRepository;

    @GetMapping("/index")
    public String listEvents(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             Model model) {
        Page<Event> eventPage = eventRepository.findAll(PageRequest.of(page, size));
        List<EventType> eventTypes = eventTypeRepository.findAll();

        model.addAttribute("eventPage", eventPage);
        model.addAttribute("eventTypes", eventTypes);
        model.addAttribute("isAdmin", true);

        return "views/admin/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("eventTypes", eventTypeRepository.findAll());
        model.addAttribute("isAdmin", true);
        return "views/admin/create";
    }

    // Step 2: Save event and redirect to question selection page
    @PostMapping("/select-questions")
    public String processCreateForm(@ModelAttribute Event event, @RequestParam String eventTypeId, Model model) {
        Optional<EventType> selectedType = eventTypeRepository.findById(eventTypeId);
        if (selectedType.isPresent()) {
            event.setEventType(selectedType.get());
            Event savedEvent = eventRepository.save(event);
            return "redirect:/admin/events/select-questions/" + savedEvent.getId();
        }
        return "redirect:/admin/events/create";
    }

    // Step 3: Show question selection page for this event
    @GetMapping("/select-questions/{eventId}")
    public String showQuestionSelection(@PathVariable String eventId, Model model) {
        Event event = eventRepository.findById(eventId).orElseThrow(); 
        List<Question> questions = questionRepository.findByEventTypeId(event.getEventType().getId());

        System.out.println("Loaded event ID: " + event.getId());
        model.addAttribute("event", event);
        model.addAttribute("questions", questions);
        model.addAttribute("newQuestion", new Question());
        model.addAttribute("isAdmin", true);
        return "views/admin/select-questions";
    }

    // Step 4: Save final event with selected questions
    @PostMapping("/save")
    public String saveEvent(@RequestParam String eventId,
                        @RequestParam(value = "questionIds", required = false) List<String> questionIds) {

    System.out.println("Saving event with ID: " + eventId);

    Event event = eventRepository.findById(eventId.trim())
            .orElseThrow(() -> new IllegalArgumentException("Event not found for ID: " + eventId));

    if (questionIds != null && !questionIds.isEmpty()) {
        List<Question> selectedQuestions = questionRepository.findAllById(questionIds);
        event.setQuestions(selectedQuestions);
    } else {
        event.setQuestions(new ArrayList<>());
    }

    eventRepository.save(event);

    return "redirect:/admin/events/index";
}


    // Step 5: Add question for current event type
    @PostMapping("/add-question")
    public String addQuestion(@ModelAttribute Question newQuestion, @RequestParam String eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        newQuestion.setEventType(event.getEventType());
        questionRepository.save(newQuestion);
        return "redirect:/admin/events/select-questions/" + eventId;
    }

    // Step 6: Delete question
    // @PostMapping("/delete-question")
    // public String deleteQuestion(@RequestParam String questionId, @RequestParam String eventId) {
    //     questionRepository.deleteById(questionId);
    //     return "redirect:/admin/events/select-questions/" + eventId;
    // }

    @GetMapping("/{eventId}/registrations")
    public String viewEventRegistrations(@PathVariable String eventId, Model model) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

        List<UserEventRegistration> registrations = registrationRepository.findByEventId(eventId);

        // Build a map of questionId → questionText
        Map<String, String> questionMap = event.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Question::getText));

        model.addAttribute("event", event);
        model.addAttribute("registrations", registrations);
        model.addAttribute("questionMap", questionMap);
        model.addAttribute("isAdmin", true);

        return "views/admin/registrations";
    }
}

