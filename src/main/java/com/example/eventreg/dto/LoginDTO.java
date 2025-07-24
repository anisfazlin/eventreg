package com.example.eventreg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginDTO
{
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9_]+$")
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^[A-Za-z0-9_]+$")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
