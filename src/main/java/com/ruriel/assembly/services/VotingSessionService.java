package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.AgendaAlreadyHasVotingSessionException;
import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.api.exceptions.VotingHasAlreadyStartedException;
import com.ruriel.assembly.api.exceptions.VotingIsFinishedException;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.AgendaRepository;
import com.ruriel.assembly.repositories.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

import static com.ruriel.assembly.api.exceptions.messages.AgendaMessages.AGENDA_ALREADY_HAS_VOTING_SESSION;
import static com.ruriel.assembly.api.exceptions.messages.AgendaMessages.AGENDA_NOT_FOUND;
import static com.ruriel.assembly.api.exceptions.messages.VotingSessionMessages.*;

@Service
@RequiredArgsConstructor
public class VotingSessionService {

    private final AgendaRepository agendaRepository;
    private final VotingSessionRepository votingSessionRepository;

    private static final int ONE_MINUTE = 60 * 1000;

    public Page<VotingSession> findPage(Pageable pageable) {
        return votingSessionRepository.findAll(pageable);
    }

    private void checkAgenda(Agenda agenda) {
        if (agenda.getVotingSession() != null) {
            var message = String.format(AGENDA_ALREADY_HAS_VOTING_SESSION, agenda.getId());
            throw new AgendaAlreadyHasVotingSessionException(message);
        }
    }

    private void checkVotingSession(VotingSession votingSession) {
        if (votingSession.isFinished()) {
            var message = String.format(VOTING_IS_FINISHED, votingSession.getId());
            throw new VotingIsFinishedException(message);
        }
        if (votingSession.hasStarted()) {
            var message = String.format(VOTING_SESSION_HAS_ALREADY_STARTED, votingSession.getId());
            throw new VotingHasAlreadyStartedException(message);
        }
    }

    private LocalDateTime getDefaultEndsAt(LocalDateTime startsAt) {
        return startsAt.plusMinutes(ONE_MINUTE);
    }

    public VotingSession create(VotingSession votingSession) {
        var now = LocalDateTime.now();
        var startsAt = votingSession.getStartsAt();
        var endsAt = votingSession.getEndsAt();
        var agendaId = votingSession.getAgenda().getId();
        var agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> {
                    var message = String.format(AGENDA_NOT_FOUND, agendaId);
                    return new ResourceNotFoundException(message);
                });
        checkAgenda(agenda);
        votingSession.setVotes(new HashSet<>());
        votingSession.setAgenda(agenda);
        votingSession.setCreatedAt(now);
        if (endsAt == null || endsAt.isBefore(startsAt))
            votingSession.setEndsAt(getDefaultEndsAt(startsAt));
        return votingSessionRepository.save(votingSession);
    }

    public VotingSession update(Long id, VotingSession votingSession) {
        final LocalDateTime now = LocalDateTime.now();
        var currentVotingSession = findById(id);
        var startsAt = votingSession.getStartsAt();
        var endsAt = votingSession.getEndsAt();
        checkVotingSession(currentVotingSession);
        currentVotingSession.setStartsAt(startsAt);
        if (endsAt == null || endsAt.isBefore(startsAt))
            currentVotingSession.setEndsAt(getDefaultEndsAt(startsAt));
        else
            currentVotingSession.setEndsAt(endsAt);
        currentVotingSession.setUpdatedAt(now);
        return votingSessionRepository.save(currentVotingSession);
    }

    public VotingSession findById(Long id) {
        return votingSessionRepository.findById(id)
                .orElseThrow(() -> {
                    var message = String.format(VOTING_SESSION_NOT_FOUND, id);
                    return new ResourceNotFoundException(message);
                });
    }

}
