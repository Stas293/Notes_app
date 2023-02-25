package com.springservice.app.notes.config;

import com.springservice.app.notes.models.Role;
import com.springservice.app.notes.models.User;
import com.springservice.app.notes.repository.RoleRepository;
import com.springservice.app.notes.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class InitDataConfig {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InitDataConfig(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner preLoadMongo() {
        return args -> {
            Role user = Role.builder()
                    .id(String.valueOf(1))
                    .name("user")
                    .code("ROLE_USER")
                    .build();
            Role admin = Role.builder()
                    .id(String.valueOf(2))
                    .name("admin")
                    .code("ROLE_ADMIN")
                    .build();
            List<Role> roles = roleRepository.saveAll(List.of(user, admin));
            User initialUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .enabled(true)
                    .roles(roles)
                    .build();
            userRepository.save(initialUser);
        };
    }
}
