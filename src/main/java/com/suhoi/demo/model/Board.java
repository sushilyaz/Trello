package com.suhoi.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
public class Board extends BaseEntity {

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> moderators;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> members;
}
