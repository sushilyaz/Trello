package com.suhoi.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "boards", schema = "task_manager")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Board extends BaseEntity {

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> moderators;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> members;
}
