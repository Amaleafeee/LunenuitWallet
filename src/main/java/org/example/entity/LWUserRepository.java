package org.example.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LWUserRepository extends MongoRepository<LWUser, String> {
    LWUser findByUsername(String username);
    boolean existsByUsername(String username);
}
