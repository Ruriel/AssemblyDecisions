package com.ruriel.assembly.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "voting_session_id")
    private VotingSession votingSession;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "associates_agendas",
            joinColumns = @JoinColumn(name = "agenda_id"),
            inverseJoinColumns = @JoinColumn(name = "associate_id"))
    private Set<Associate> associates;

    private Boolean enabled;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Boolean hasAssociate(Long associateId){
        return associates.stream().anyMatch(associate -> Objects.equals(associate.getId(), associateId));
    }

}
