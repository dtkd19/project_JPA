package com.sh.project_JPA.repository;

import com.sh.project_JPA.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    List<Auth> findByEmail(String email);
}
