package com.ruriel.assembly.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VotingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime endsAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
    @OneToOne(mappedBy = "votingSession", cascade = CascadeType.MERGE)
    private Agenda agenda;

    @OneToMany(mappedBy = "votingSession")
    private Set<Vote> votes;

    public void setAgenda(Agenda agenda){
        this.agenda = agenda;
        agenda.setVotingSession(this);
    }
    public void setStartsAt(LocalDateTime startsAt) {
        var now = LocalDateTime.now();
        if (startsAt == null || startsAt.isBefore(now))
            this.startsAt = now;
        else
            this.startsAt = startsAt;
    }

    public Long getTotalVotes(){
        if(hasStarted())
            return (long) votes.size();
        return null;
    }
    public Long getYesVotes() {
        if (hasStarted())
            return votes.stream().filter(Vote::getContent).count();
        return null;
    }

    public Long getNoVotes() {
        if (hasStarted())
            return votes.stream().filter(vote -> !vote.getContent()).count();
        return null;
    }

    public boolean hasStarted() {
        var now = LocalDateTime.now();
        return now.isAfter(startsAt);
    }

    public boolean isFinished() {
        var now = LocalDateTime.now();
        return now.isAfter(endsAt);
    }

    @Enumerated(EnumType.STRING)
    public ResultType getResult() {
        if (!hasStarted())
            return ResultType.NOT_STARTED;
        if (!isFinished())
            return ResultType.COUNTING;
        var yesVotes = getYesVotes();
        var noVotes = getNoVotes();
        if (yesVotes > noVotes)
            return ResultType.YES;
        if (noVotes > yesVotes)
            return ResultType.NO;
        return ResultType.DRAW;
    }

    public Boolean hasAssociateVoted(Associate associate){
        return votes.stream().anyMatch(vote -> vote.hasAssociate(associate.getId()));
    }
}
