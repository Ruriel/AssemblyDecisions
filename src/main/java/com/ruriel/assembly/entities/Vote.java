package com.ruriel.assembly.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "associate_id", "voting_session_id" }) })
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "associate_id")
    private Associate associate;

    @Column(nullable = false)
    private Boolean content;

    @ManyToOne
    @JoinColumn(name="voting_session_id", nullable=false)
    private VotingSession votingSession;
}
