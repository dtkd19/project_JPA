package com.sh.project_JPA.repository;

import com.sh.project_JPA.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
