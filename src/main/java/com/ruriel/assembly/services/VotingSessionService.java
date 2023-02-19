package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.VotingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;

@Service
public class VotingSessionService {
    @Autowired
    private VotingSessionRepository votingSessionRepository;

    private static final int ONE_MINUTE = 60 * 1000;
    private static final String VOTING_SESSION_NOT_FOUND = "No voting session with id %d found.";
    public Page<VotingSession> findPage(Pageable pageable) {
        return votingSessionRepository.findAll(pageable);
    }

    public VotingSession create(VotingSession votingSession) {
        var startsAt = votingSession.getStartsAt();
        var endsAt = votingSession.getEndsAt();
        if(endsAt == null || endsAt.before(startsAt))
            votingSession.setEndsAt(new Date(startsAt.getTime() + ONE_MINUTE));
        votingSession.setVotes(new HashSet<>());
        return votingSessionRepository.save(votingSession);
    }

    public VotingSession findById(Long id) {
        return votingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(VOTING_SESSION_NOT_FOUND, id)));
    }

}
