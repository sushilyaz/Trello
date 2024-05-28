package com.suhoi.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cards", schema = "task_manager")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Card extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Status status;

    private String importance;

    @JsonIgnore
    @ManyToMany
    private List<User> assignees;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "card_lists_id")
    private CardList cardList;

    @JsonBackReference
    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<Task> task;

    private LocalDate deadline;

    private boolean burned;
}
