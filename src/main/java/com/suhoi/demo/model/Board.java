package com.suhoi.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NamedEntityGraph(
        name = "GetModeratorsAndMembers",
        attributeNodes = {
                @NamedAttributeNode("moderators"),
                @NamedAttributeNode("members")
        }
)
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
