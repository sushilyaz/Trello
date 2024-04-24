package com.suhoi.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.Mapping;

@Entity
@Table(name = "tasks", schema = "task_manager")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Task extends BaseEntity {

    @Column(name = "is_complete")
    private boolean isComplete;

    @ManyToOne
    private Card card;
}
