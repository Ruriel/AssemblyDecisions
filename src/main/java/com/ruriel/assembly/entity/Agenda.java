package com.ruriel.assembly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "agenda_id")
    private Set<VotingSession> votingSessions;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "associates_agendas")
    private Set<Associate> associates;

    private Boolean enabled;

    private Date createdAt;

    private Date updatedAt;
}