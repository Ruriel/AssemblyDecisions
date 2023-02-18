package com.ruriel.assembly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String associate;

    @Column(nullable = false)
    private Boolean content;

    @ManyToOne
    @JoinColumn(name="voting_session_id", nullable=false)
    private VotingSession votingSession;
}
