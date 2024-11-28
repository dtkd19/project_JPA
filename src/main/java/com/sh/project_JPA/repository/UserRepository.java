package com.sh.project_JPA.repository;

import com.sh.project_JPA.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
