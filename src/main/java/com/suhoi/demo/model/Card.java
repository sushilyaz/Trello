package com.suhoi.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Card extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Status status;

    private String importance;

    @ManyToMany
    private List<User> assignees;

    @ManyToOne(fetch = FetchType.LAZY)
    private CardList cardList;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<Task> task;

    private LocalDate deadline;
}
