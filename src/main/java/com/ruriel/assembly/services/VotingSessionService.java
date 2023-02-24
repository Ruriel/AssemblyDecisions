package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.*;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.AgendaRepository;
import com.ruriel.assembly.repositories.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

import static com.ruriel.assembly.api.exceptions.messages.AgendaMessages.*;
import static com.ruriel.assembly.api.exceptions.messages.VotingSessionMessages.*;

@Service
@RequiredArgsConstructor
public class VotingSessionService {

    private final AgendaRepository agendaRepository;
    private final VotingSessionRepository votingSessionRepository;

    private static final String TIME_MUST_BE_IN_THE_FUTURE_MESSAGE = "The date can't be from the past.";
    public Page<VotingSession> findPage(Pageable pageable) {
        return votingSessionRepository.findAll(pageable);
    }

    private Agenda findAgenda(Agenda agenda) {
        var id = agenda.getId();
        var foundAgenda = agendaRepository.findById(id)
                .orElseThrow(() -> {
                    var message = String.format(AGENDA_NOT_FOUND, id);
                    return new ResourceNotFoundException(message);
                });
        if (foundAgenda.getVotingSession() != null) {
            var message = String.format(AGENDA_ALREADY_HAS_VOTING_SESSION, id);
            throw new AgendaAlreadyHasVotingSessionException(message);
        }
        if (Boolean.TRUE.equals(foundAgenda.hasNoAssociates())) {
            var message = String.format(AGENDA_HAS_NO_ASSOCIATES_EXCEPTION, id);
            throw new AgendaHasNoAssociatesException(message);
        }
        return foundAgenda;
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
        return startsAt.plusMinutes(1);
    }

    public VotingSession create(VotingSession votingSession) {
        final LocalDateTime now = LocalDateTime.now();
        var agenda = findAgenda(votingSession.getAgenda());
        var startsAt = votingSession.getStartsAt();
        var endsAt = votingSession.getEndsAt() == null ? getDefaultEndsAt(startsAt) : votingSession.getEndsAt();
        if (startsAt.isBefore(now) || endsAt.isBefore(now) || endsAt.isBefore(startsAt))
            throw new TimeMustBeInTheFutureException(TIME_MUST_BE_IN_THE_FUTURE_MESSAGE);
        votingSession.setVotes(new HashSet<>());
        votingSession.setAgenda(agenda);
        votingSession.setCreatedAt(now);
        votingSession.setEndsAt(endsAt);
        return votingSessionRepository.save(votingSession);
    }

    public VotingSession update(Long id, VotingSession votingSession) {
        final LocalDateTime now = LocalDateTime.now();
        var currentVotingSession = findById(id);
        var startsAt = votingSession.getStartsAt();
        var endsAt = votingSession.getEndsAt() == null ? getDefaultEndsAt(startsAt) : votingSession.getEndsAt();
        checkVotingSession(currentVotingSession);
        if (startsAt.isBefore(now) || endsAt.isBefore(now) || endsAt.isBefore(startsAt))
            throw new TimeMustBeInTheFutureException(TIME_MUST_BE_IN_THE_FUTURE_MESSAGE);
        currentVotingSession.setStartsAt(startsAt);
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
