package com.tinqinacademy.authentication.core.security;

import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Not valid username or password"));

        // User's role
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), List.of(grantedAuthority));
    }
}
