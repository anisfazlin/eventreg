package com.example.eventreg.controller;

import com.example.eventreg.dto.LoginDTO;
import com.example.eventreg.dto.SignupDTO;
import com.example.eventreg.model.User;
import com.example.eventreg.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    public UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "views/login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            return "views/login";
        }

        Optional<User> userOpt = userRepository.findByUsername(loginDTO.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getPassword().equals(loginDTO.getPassword())) {

                if ("admin".equalsIgnoreCase(user.getRole())) {
                    return "redirect:/admin/events/index";
                } else if ("user".equalsIgnoreCase(user.getRole())) {
                    return "redirect:/user/events/index/" + user.getUsername();
                } else {
                    model.addAttribute("error", "Unrecognized user role");
                    return "views/login";
                }

            } else {
                model.addAttribute("error", "Invalid password");
            }
        } else {
            model.addAttribute("error", "User not found");
        }

        return "views/login";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupDTO", new SignupDTO());
        return "views/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute("signupDTO") SignupDTO signupDTO,
                                BindingResult result,
                                Model model) {

        // Check if passwords match
        if (!signupDTO.getPassword().equals(signupDTO.getConfirmPassword())) {
            result.addError(new FieldError("signupDTO", "confirmPassword", "Passwords do not match"));
        }

        // Check if username already exists
        if (userRepository.findByUsername(signupDTO.getUsername()).isPresent()) {
            result.addError(new FieldError("signupDTO", "username", "Username already exists"));
        }

        if (result.hasErrors()) {
            return "views/signup";
        }

        // Create new user with role "user"
        User newUser = new User();
        newUser.setUsername(signupDTO.getUsername());
        newUser.setPassword(signupDTO.getPassword()); // In a real app, hash the password
        newUser.setEmail(signupDTO.getEmail());
        newUser.setRole("user"); // Always create regular users through signup

        userRepository.save(newUser);

        // Redirect to login page with success message
        model.addAttribute("successMessage", "Registration successful! Please login.");
        return "redirect:/login?registered=true";
    }

    @GetMapping("/logout")
    public String logout() {
        // In a real app with session management, invalidate the session here
        return "redirect:/login?logout=true";
    }

}


