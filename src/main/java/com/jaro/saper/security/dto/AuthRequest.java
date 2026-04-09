package com.jaro.saper.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "Login nie może być pusty")
    @Size(min = 3, max = 20, message = "Login musi mieć od 3 do 20 znaków")
    private String login;

    @NotBlank(message = "Hasło nie może być puste")
    @Size(min = 6, message = "Hasło musi mieć minimum 6 znaków")
    private String password;
}