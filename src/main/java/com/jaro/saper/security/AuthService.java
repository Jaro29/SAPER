package com.jaro.saper.security;

import com.jaro.saper.model.User;
import com.jaro.saper.repository.UserRepository;
import com.jaro.saper.security.dto.AuthRequest;
import com.jaro.saper.security.dto.AuthResponse;
import com.jaro.saper.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByLogin(request.getLogin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login jest już zajęty");
        }

        User user = new User();
        user.setLogin(request.getLogin());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getLogin());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getLogin());
        return new AuthResponse(token);
    }
}