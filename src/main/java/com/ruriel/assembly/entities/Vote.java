package com.ruriel.assembly.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "associate_id", "voting_session_id" }) })
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "associate_id")
    private Associate associate;

    @Column(nullable = false)
    private Boolean content;

    @ManyToOne
    @JoinColumn(name="voting_session_id", nullable=false)
    private VotingSession votingSession;

    @Column(nullable = false)
    private Date createdAt;

    private Date updatedAt;

    public Boolean hasAssociate(Long id){
        return Objects.equals(associate.getId(), id);
    }
}
