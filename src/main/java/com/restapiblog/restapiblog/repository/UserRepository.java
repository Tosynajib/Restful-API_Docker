package com.restapiblog.restapiblog.repository;

import com.restapiblog.restapiblog.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Users, Long> {
     UserDetails findUserByUsername(String username);

    Optional<UserDetails> findByUsername(String username);
}
