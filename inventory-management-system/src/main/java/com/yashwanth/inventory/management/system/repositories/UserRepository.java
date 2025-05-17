package com.yashwanth.inventory.management.system.repositories;

import com.yashwanth.inventory.management.system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
