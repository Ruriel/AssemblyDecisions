package com.ruriel.assembly.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "associate_id", "voting_session_id" }) })
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associate_id", nullable=false)
    private Associate associate;

    @Column(nullable = false)
    private Boolean content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="voting_session_id", nullable=false)
    private VotingSession votingSession;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Boolean hasAssociate(Long id){
        return Objects.equals(associate.getId(), id);
    }
}
