package com.jaro.saper.security;

import com.jaro.saper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        com.jaro.saper.model.User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie istnieje: " + login));

        return User.builder()
                .username(user.getLogin())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
    }
}