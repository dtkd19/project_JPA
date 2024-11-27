package com.sh.project_JPA.repository;

import com.sh.project_JPA.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    boolean existsByUuid(String uuid);

    List<File> findAllByBno(long bno);

    List<File> findByBno(Long bno);

    void deleteByUuid(String uuid);
}
