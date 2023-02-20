package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.AgendaAlreadyHasVotingSessionException;
import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.api.exceptions.VotingHasAlreadyStartedException;
import com.ruriel.assembly.api.exceptions.VotingIsFinishedException;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.AgendaRepository;
import com.ruriel.assembly.repositories.VotingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;

import static com.ruriel.assembly.api.exceptions.messages.AgendaMessages.AGENDA_ALREADY_HAS_VOTING_SESSION;
import static com.ruriel.assembly.api.exceptions.messages.AgendaMessages.AGENDA_NOT_FOUND;
import static com.ruriel.assembly.api.exceptions.messages.VotingSessionMessages.*;

@Service
public class VotingSessionService {

    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private VotingSessionRepository votingSessionRepository;

    private static final int ONE_MINUTE = 60 * 1000;

    public Page<VotingSession> findPage(Pageable pageable) {
        return votingSessionRepository.findAll(pageable);
    }

    private void checkAgenda(Agenda agenda) {
        if (agenda.getVotingSession() != null)
            throw new AgendaAlreadyHasVotingSessionException(String.format(AGENDA_ALREADY_HAS_VOTING_SESSION, agenda.getId()));
    }

    private void checkVotingSession(VotingSession votingSession) {
        if (votingSession.isFinished())
            throw new VotingIsFinishedException(String.format(VOTING_IS_FINISHED, votingSession.getId()));
        if (votingSession.hasStarted())
            throw new VotingHasAlreadyStartedException(String.format(VOTING_SESSION_HAS_ALREADY_STARTED, votingSession.getId()));
    }

    private Date getDefaultEndsAt(Date startsAt) {
        return new Date(startsAt.getTime() + ONE_MINUTE);
    }

    public VotingSession create(VotingSession votingSession) {
        var agendaId = votingSession.getAgenda().getId();
        var agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(AGENDA_NOT_FOUND, agendaId)));
        checkAgenda(agenda);
        var startsAt = votingSession.getStartsAt();
        var endsAt = votingSession.getEndsAt();
        votingSession.setVotes(new HashSet<>());
        votingSession.setAgenda(agenda);
        votingSession.setCreatedAt(Date.from(Instant.now()));
        if (endsAt == null || endsAt.before(startsAt))
            votingSession.setEndsAt(getDefaultEndsAt(startsAt));
        return votingSessionRepository.save(votingSession);
    }

    public VotingSession update(Long id, VotingSession votingSession) {
        var currentVotingSession = votingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(VOTING_SESSION_NOT_FOUND, id)));
        checkVotingSession(currentVotingSession);
        var startsAt = votingSession.getStartsAt();
        var endsAt = votingSession.getEndsAt();
        currentVotingSession.setStartsAt(startsAt);
        if (endsAt == null || endsAt.before(startsAt))
            currentVotingSession.setEndsAt(getDefaultEndsAt(startsAt));
        else
            currentVotingSession.setEndsAt(endsAt);
        return currentVotingSession;
    }

    public VotingSession findById(Long id) {
        return votingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(VOTING_SESSION_NOT_FOUND, id)));
    }

}
