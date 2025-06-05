package com.example.inventory_h2.repository;

import com.example.inventory_h2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

