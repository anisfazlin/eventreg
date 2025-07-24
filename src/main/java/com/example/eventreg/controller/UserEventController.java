package com.example.eventreg.controller;

import com.example.eventreg.dto.AnswerDTO;
import com.example.eventreg.dto.RegistrationFormDTO;
import com.example.eventreg.model.Event;
import com.example.eventreg.model.User;
import com.example.eventreg.model.UserEventRegistration;
import com.example.eventreg.repository.EventRepository;
import com.example.eventreg.repository.UserEventRegistrationRepository;
import com.example.eventreg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user/events")
public class UserEventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEventRegistrationRepository registrationRepository;

    @GetMapping("/index/{username}")
    public String showAvailableEvents(@PathVariable String username, Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", false);

        return "views/users/index";
    }

    @GetMapping("/registered/{username}")
    public String showUserRegistrations(@PathVariable String username, Model model) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<UserEventRegistration> registrations = registrationRepository.findByUserId(user.getId());

        model.addAttribute("registrations", registrations);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", false);
        return "views/users/registered";
    }

    @GetMapping("/register/{eventId}/{username}")
    public String showRegistrationForm(@PathVariable String eventId,
                                       @PathVariable String username,
                                       Model model) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + eventId));

        List<AnswerDTO> answerDTOs = event.getQuestions().stream()
                .map(q -> new AnswerDTO(q.getId(), q.getText(),""))
                .toList();

        RegistrationFormDTO formDTO = new RegistrationFormDTO();
        formDTO.setAnswers(answerDTOs);

        model.addAttribute("registrationForm", formDTO);
        model.addAttribute("event", event);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", false);

        return "views/users/register";
    }

    @PostMapping("/register/{eventId}/{username}")
    public String submitRegistration(@PathVariable String eventId,
                                     @PathVariable String username,
                                     @ModelAttribute RegistrationFormDTO registrationForm) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserEventRegistration registration = new UserEventRegistration();
        registration.setEvent(event);
        registration.setUser(user);
        registration.setRegisteredAt(LocalDateTime.now());

        registration.setName(registrationForm.getName());
        registration.setEmail(registrationForm.getEmail());
        registration.setPhoneNumber(registrationForm.getPhoneNumber());

        registration.setAnswers(registrationForm.getAnswers());

        registrationRepository.save(registration);

        return "redirect:/user/events/confirmation/" + eventId + "/" + username;
    }

    @GetMapping("/confirmation/{eventId}/{username}")
    public String showConfirmation(@PathVariable String eventId,
                                   @PathVariable String username,
                                   Model model) {
        model.addAttribute("username", username);
        model.addAttribute("eventId", eventId);
        model.addAttribute("isAdmin", false);

        return "views/users/confirmation";
    }
}

