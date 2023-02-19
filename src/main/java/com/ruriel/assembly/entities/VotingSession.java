package com.ruriel.assembly.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Date;
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
    private Date startsAt;

    @Column(nullable = false)
    private Date endsAt;

    @CreationTimestamp
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @OneToMany(mappedBy = "votingSession")
    private Set<Vote> votes;

    public void setStartsAt(Date startsAt) {
        var now = Date.from(Instant.now());
        if (startsAt == null || startsAt.before(now))
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
        var now = Date.from(Instant.now());
        return now.after(startsAt);
    }

    public boolean isFinished() {
        var now = Date.from(Instant.now());
        return now.after(endsAt);
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
}
