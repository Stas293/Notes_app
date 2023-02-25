package com.springservice.app.notes.repository;

import com.springservice.app.notes.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByCode(String code);
}
