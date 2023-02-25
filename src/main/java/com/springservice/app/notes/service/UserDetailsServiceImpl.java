package com.springservice.app.notes.service;

import com.springservice.app.notes.constants.ExceptionConstants;
import com.springservice.app.notes.repository.UserRepository;
import com.springservice.app.notes.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(
                userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionConstants.USER_NOT_FOUND))
        );
    }
}
