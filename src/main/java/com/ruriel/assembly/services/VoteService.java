package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.*;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.entities.Vote;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.VoteRepository;
import com.ruriel.assembly.repositories.VotingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

import static com.ruriel.assembly.api.exceptions.messages.AssociateMessages.ASSOCIATED_VOTED_ALREADY;
import static com.ruriel.assembly.api.exceptions.messages.AssociateMessages.ASSOCIATE_NOT_REGISTERED;
import static com.ruriel.assembly.api.exceptions.messages.VoteMessages.VOTE_NOT_FOUND;
import static com.ruriel.assembly.api.exceptions.messages.VotingSessionMessages.*;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VotingSessionRepository votingSessionRepository;

    private void checkVotingSession(VotingSession votingSession, Associate associate){
        var agenda = votingSession.getAgenda();
        if (!votingSession.hasStarted()) {
            throw new VotingHasNotStartedException(String.format(VOTING_HAS_NOT_STARTED, votingSession.getId()));
        }
        if (votingSession.isFinished()) {
            throw new VotingIsFinishedException(String.format(VOTING_IS_FINISHED, votingSession.getId()));
        }
        if(!agenda.hasAssociate(associate.getId())) {
            throw new AssociateNotRegisteredInAgendaException(String.format(ASSOCIATE_NOT_REGISTERED, associate.getId()));
        }
    }
    private VotingSession findVotingSession(Long id, Associate associate) {
        var foundVotingSession = votingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(VOTING_SESSION_NOT_FOUND, id)));
        checkVotingSession(foundVotingSession, associate);
        return foundVotingSession;
    }

    public Vote create(Long votingSessionId, Vote vote) {
        var associate = vote.getAssociate();
        var votingSession = findVotingSession(votingSessionId, associate);
        if(votingSession.hasAssociateVoted(associate)) {
            throw new AssociateAlreadyVotedException(String.format(ASSOCIATED_VOTED_ALREADY, associate.getId()));
        }
        vote.setVotingSession(votingSession);
        vote.setCreatedAt(Date.from(Instant.now()));
        return voteRepository.save(vote);
    }

    public Vote patch(Long votingSessionId, Long id, Vote vote) {
        var currentVote = voteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(VOTE_NOT_FOUND, id)));
        findVotingSession(votingSessionId, vote.getAssociate());
        currentVote.setContent(vote.getContent());
        currentVote.setUpdatedAt(Date.from(Instant.now()));
        return voteRepository.save(currentVote);
    }
}
