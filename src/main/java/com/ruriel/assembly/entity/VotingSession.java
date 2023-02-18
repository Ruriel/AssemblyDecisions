package com.ruriel.assembly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date duration;

    @ManyToOne
    @JoinColumn(name="agenda_id", nullable=false)
    private Agenda agenda;

    @OneToMany(mappedBy = "votingSession")
    private Set<Vote> votes = new HashSet<>();

    public Long getYesVotes(){
        return votes.stream().filter(Vote::getContent).count();
    }

    public Long getNoVotes(){
        return votes.stream().filter(vote -> !vote.getContent()).count();
    }

    @Enumerated(EnumType.STRING)
    public ResultType getResult(){
        var yesVotes = getYesVotes();
        var noVotes = getNoVotes();
        if(yesVotes > noVotes)
            return ResultType.YES;
        if(noVotes > yesVotes)
            return ResultType.NO;
        return ResultType.DRAW;
    }
}
