package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.*;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.entities.Vote;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.VoteRepository;
import com.ruriel.assembly.repositories.VotingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceTests {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @InjectMocks
    private VoteService voteService;

    @Test
    void shouldCreate(){
        var id = 1L;
        var startsAt = LocalDateTime.now().minusMinutes(10);
        var endsAt =LocalDateTime.now().plusMinutes(10);
        var associate = Associate.builder().id(id).name("Testing").build();
        var agenda = Agenda.builder().id(id).associates(Set.of(associate)).build();
        var votingSession = VotingSession.builder()
                .agenda(agenda)
                .startsAt(startsAt)
                .endsAt(endsAt)
                .votes(new HashSet<>())
                .build();
        var vote = Vote.builder().associate(associate).content(true).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(votingSession));
        voteService.create(id, vote);
        verify(voteRepository).save(vote);
    }

    @Test
    void shouldNotCreateAndThrowNotFound(){
        var id = 1L;
        var associate = Associate.builder().id(id).name("Testing").build();
        var vote = Vote.builder().associate(associate).content(true).build();
        assertThatThrownBy(() -> voteService.create(id, vote)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldNotCreateBecauseVotingHasNotStarted(){
        var id = 1L;
        var startsAt = LocalDateTime.now().plusMinutes(10);
        var endsAt = startsAt.plusMinutes(10);
        var associate = Associate.builder().id(id).name("Testing").build();
        var agenda = Agenda.builder().id(id).associates(Set.of(associate)).build();
        var votingSession = VotingSession.builder()
                .agenda(agenda)
                .startsAt(startsAt)
                .endsAt(endsAt)
                .votes(new HashSet<>())
                .build();
        var vote = Vote.builder().associate(associate).content(true).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(votingSession));
        assertThatThrownBy(() -> voteService.create(id, vote)).isInstanceOf(VotingHasNotStartedException.class);
    }

    @Test
    void shouldNotCreateBecauseVotingHasEnded(){
        var id = 1L;
        var startsAt = LocalDateTime.now().minusMinutes(10);
        var endsAt = startsAt.plusMinutes(1);
        var associate = Associate.builder().id(id).name("Testing").build();
        var agenda = Agenda.builder().id(id).associates(Set.of(associate)).build();
        var votingSession = VotingSession.builder()
                .agenda(agenda)
                .startsAt(startsAt)
                .endsAt(endsAt)
                .votes(new HashSet<>())
                .build();
        var vote = Vote.builder().associate(associate).content(true).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(votingSession));
        assertThatThrownBy(() -> voteService.create(id, vote)).isInstanceOf(VotingIsFinishedException.class);
    }

    @Test
    void shouldNotCreateBecauseAssociateIsNotPartOfAgenda(){
        var id = 1L;
        var startsAt = LocalDateTime.now().minusMinutes(10);
        var endsAt =LocalDateTime.now().plusMinutes(10);
        var associate = Associate.builder().id(id).name("Testing").build();
        var agenda = Agenda.builder().id(id).associates(new HashSet<>()).build();
        var votingSession = VotingSession.builder()
                .agenda(agenda)
                .startsAt(startsAt)
                .endsAt(endsAt)
                .votes(new HashSet<>())
                .build();
        var vote = Vote.builder().associate(associate).content(true).build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(votingSession));
        assertThatThrownBy(() -> voteService.create(id, vote)).isInstanceOf(AssociateNotRegisteredInAgendaException.class);
    }

    @Test
    void shouldNotCreateBecauseAssociateAlreadyVoted(){
        var id = 1L;
        var startsAt = LocalDateTime.now().minusMinutes(10);
        var endsAt =LocalDateTime.now().plusMinutes(10);
        var associate = Associate.builder().id(id).name("Testing").build();
        var agenda = Agenda.builder().id(id).associates(Set.of(associate)).build();
        var vote = Vote.builder().associate(associate).content(true).build();
        var votingSession = VotingSession.builder()
                .agenda(agenda)
                .startsAt(startsAt)
                .endsAt(endsAt)
                .votes(Set.of(vote))
                .build();
        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(votingSession));
        assertThatThrownBy(() -> voteService.create(id, vote)).isInstanceOf(AssociateAlreadyVotedException.class);
    }
}
