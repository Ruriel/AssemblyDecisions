package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.*;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.entities.Vote;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.VoteRepository;
import com.ruriel.assembly.repositories.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.ruriel.assembly.api.exceptions.messages.AssociateMessages.*;
import static com.ruriel.assembly.api.exceptions.messages.VotingSessionMessages.*;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final VotingSessionRepository votingSessionRepository;

    private void checkVotingSession(VotingSession votingSession, Associate associate){
        if (!votingSession.hasStarted()) {
            var message = String.format(VOTING_HAS_NOT_STARTED, votingSession.getId());
            throw new VotingHasNotStartedException(message);
        }
        if (votingSession.isFinished()) {
            var message = String.format(VOTING_IS_FINISHED, votingSession.getId());
            throw new VotingIsFinishedException(message);
        }
        if(Boolean.FALSE.equals(votingSession.hasAssociate(associate))) {
            var message = String.format(ASSOCIATE_NOT_REGISTERED, associate.getId());
            throw new AssociateNotRegisteredInAgendaException(message);
        }
    }
    private VotingSession findVotingSession(Long id, Associate associate) {
        var foundVotingSession = votingSessionRepository.findById(id)
                .orElseThrow(() -> {
                    var message = String.format(VOTING_SESSION_NOT_FOUND, id);
                    return new ResourceNotFoundException(message);
                });
        checkVotingSession(foundVotingSession, associate);
        return foundVotingSession;
    }

    public Vote create(Long votingSessionId, Vote vote) {
        var now = LocalDateTime.now();
        var associate = vote.getAssociate();
        var votingSession = findVotingSession(votingSessionId, associate);
        if(Boolean.TRUE.equals(votingSession.hasVoted(associate))) {
            var message = String.format(ASSOCIATED_VOTED_ALREADY, associate.getId());
            throw new AssociateAlreadyVotedException(message);
        }
        vote.setVotingSession(votingSession);
        vote.setCreatedAt(now);
        return voteRepository.save(vote);
    }

}
