package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.*;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.AgendaRepository;
import com.ruriel.assembly.repositories.VotingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotingSessionServiceTests {
    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private AgendaRepository agendaRepository;
    @InjectMocks
    private VotingSessionService votingSessionService;

    @Test
    void shouldFindPage() {
        var pageable = PageRequest.of(0, 5);
        votingSessionService.findPage(pageable);
        verify(votingSessionRepository).findAll(pageable);
    }

    @Test
    void shouldCreate() {
        var id = 1L;
        var associate = Associate.builder().id(id).build();
        var agenda = Agenda.builder().id(id).associates(Set.of(associate)).build();
        var startsAt = LocalDateTime.now().plusMinutes(10);
        var votingSession = VotingSession.builder().agenda(agenda).startsAt(startsAt).build();
        when(agendaRepository.findById(id)).thenReturn(Optional.of(agenda));
        votingSessionService.create(votingSession);
        verify(votingSessionRepository).save(votingSession);
    }

    @Test
    void shouldNotCreateAndThrowTimeMustBeInTheFutureException() {
        var id = 1L;
        var associate = Associate.builder().id(id).build();
        var agenda = Agenda.builder().id(id).associates(Set.of(associate)).build();
        var startsAt = LocalDateTime.now().minusMinutes(10);
        var votingSession = VotingSession.builder().agenda(agenda).startsAt(startsAt).build();
        when(agendaRepository.findById(id)).thenReturn(Optional.of(agenda));
        assertThatThrownBy(() -> votingSessionService.create(votingSession)).isInstanceOf(TimeMustBeInTheFutureException.class);
    }

    @Test
    void shouldNotCreateAndThrowNoAssociatesException() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).build();
        var votingSession = VotingSession.builder().agenda(agenda).startsAt(LocalDateTime.now()).build();
        when(agendaRepository.findById(id)).thenReturn(Optional.of(agenda));
        assertThatThrownBy(() -> votingSessionService.create(votingSession)).isInstanceOf(AgendaHasNoAssociatesException.class);
    }

    @Test
    void shouldNotCreateAndThrowNotFound() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).build();
        var votingSession = VotingSession.builder().agenda(agenda).startsAt(LocalDateTime.now()).build();
        assertThatThrownBy(() -> votingSessionService.create(votingSession)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldNotCreateAndThrowAlreadyHasVotingSession() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).build();
        var votingSession = VotingSession.builder().agenda(agenda).startsAt(LocalDateTime.now()).build();
        var anotherVotingSession = VotingSession.builder().agenda(agenda).startsAt(LocalDateTime.now()).build();
        agenda.setVotingSession(anotherVotingSession);
        when(agendaRepository.findById(id)).thenReturn(Optional.of(agenda));
        assertThatThrownBy(() -> votingSessionService.create(votingSession)).isInstanceOf(AgendaAlreadyHasVotingSessionException.class);
    }

    @Test
    void shouldUpdate() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).build();
        var startsAt = LocalDateTime.now().plusMinutes(10);
        var endsAt = startsAt.plusMinutes(1);
        var currentStartsAt = startsAt.minusMinutes(5);
        var currentEndsAt = endsAt.minusMinutes(5);
        var currentSession = VotingSession.builder().agenda(agenda).startsAt(currentStartsAt).endsAt(currentEndsAt).build();
        var votingSession = VotingSession.builder().startsAt(startsAt).endsAt(endsAt).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(currentSession));
        votingSessionService.update(id, votingSession);
        verify(votingSessionRepository).save(currentSession);
    }

    @Test
    void shouldNotUpdateAndThrowTimeMustBeInTheFutureException() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).build();
        var startsAt = LocalDateTime.now().minusMinutes(10);
        var endsAt = startsAt.plusMinutes(1);
        var currentStartsAt = LocalDateTime.now().plusMinutes(5);
        var currentEndsAt = currentStartsAt.plusMinutes(1);
        var currentSession = VotingSession.builder().agenda(agenda).startsAt(currentStartsAt).endsAt(currentEndsAt).build();
        var votingSession = VotingSession.builder().startsAt(startsAt).endsAt(endsAt).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(currentSession));
        assertThatThrownBy(() -> votingSessionService.update(id, votingSession)).isInstanceOf(TimeMustBeInTheFutureException.class);
    }

    @Test
    void shouldNotUpdateBecauseIsFinished() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).build();
        var currentStartsAt = LocalDateTime.now().minusMinutes(10);
        var currentEndsAt = currentStartsAt.plusMinutes(1);
        var startsAt = LocalDateTime.now().plusMinutes(10);
        var endsAt = startsAt.plusMinutes(1);
        var currentSession = VotingSession.builder().agenda(agenda).startsAt(currentStartsAt).endsAt(currentEndsAt).build();
        var votingSession = VotingSession.builder().startsAt(startsAt).endsAt(endsAt).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(currentSession));
        assertThatThrownBy(() -> votingSessionService.update(id, votingSession)).isInstanceOf(VotingIsFinishedException.class);
    }

    @Test
    void shouldNotUpdateBecauseHasStarted() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).build();
        var currentStartsAt = LocalDateTime.now();
        var currentEndsAt = currentStartsAt.plusMinutes(10);
        var startsAt = LocalDateTime.now().plusMinutes(10);
        var endsAt = startsAt.plusMinutes(1);
        var currentSession = VotingSession.builder().agenda(agenda).startsAt(currentStartsAt).endsAt(currentEndsAt).build();
        var votingSession = VotingSession.builder().startsAt(startsAt).endsAt(endsAt).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(currentSession));
        assertThatThrownBy(() -> votingSessionService.update(id, votingSession)).isInstanceOf(VotingHasAlreadyStartedException.class);
    }

    @Test
    void shouldFindById() {
        var id = 1L;
        var votingSession = VotingSession.builder().id(id).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(votingSession));
        votingSessionService.findById(id);
        verify(votingSessionRepository).findById(id);
    }

    @Test
    void shouldNotFindById() {
        var id = 1L;
        assertThatThrownBy(() -> votingSessionService.findById(id)).isInstanceOf(ResourceNotFoundException.class);
    }
}
