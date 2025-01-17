package com.sh.project_JPA.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
public class Timebase {
    @CreatedDate
    @Column(name = "reg_at", updatable = false)
    private LocalDateTime regAt;
    @Column(name = "mod_at")
    private LocalDateTime modAt;
}
