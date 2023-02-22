package com.ruriel.assembly.controllers;

import com.ruriel.assembly.api.v1.controllers.VotingSessionController;
import com.ruriel.assembly.api.v1.resources.VoteRequest;
import com.ruriel.assembly.api.v1.resources.VotingSessionPatchRequest;
import com.ruriel.assembly.api.v1.resources.VotingSessionRequest;
import com.ruriel.assembly.entities.Vote;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.services.VoteService;
import com.ruriel.assembly.services.VotingSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotingSessionControllerTests {
    @Mock
    private VoteService voteService;

    @Mock
    private VotingSessionService votingSessionService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VotingSessionController votingSessionController;

    @Test
    void shouldGetAPage() {
        var pageable = PageRequest.of(0, 5);
        votingSessionController.findPage(pageable);
        verify(votingSessionService).findPage(pageable);
    }

    @Test
    void shouldFindById() {
        var id = 1L;
        votingSessionController.findById(id);
        verify(votingSessionService).findById(id);
    }

    @Test
    void shouldCreate() {
        var id = 1L;
        var votingSession = VotingSession.builder().id(id).votes(new HashSet<>()).build();
        var votingSessionRequest = new VotingSessionRequest();
        when(modelMapper.map(votingSessionRequest, VotingSession.class)).thenReturn(votingSession);
        votingSessionController.create(votingSessionRequest);
        verify(votingSessionService).create(votingSession);
    }

    @Test
    void shouldUpdate() {
        var id = 1L;
        var votingSession = VotingSession.builder().id(id).votes(new HashSet<>()).build();
        var votingSessionPatchRequest = new VotingSessionPatchRequest();
        when(modelMapper.map(votingSessionPatchRequest, VotingSession.class)).thenReturn(votingSession);
        votingSessionController.update(id, votingSessionPatchRequest);
        verify(votingSessionService).update(id, votingSession);
    }

    @Test
    void shouldVote() {
        var id = 1L;
        var voteRequest = new VoteRequest();
        var votingSession = VotingSession.builder().id(id).votes(new HashSet<>()).build();
        var vote = Vote.builder().votingSession(votingSession).build();
        when(modelMapper.map(voteRequest, Vote.class)).thenReturn(vote);
        votingSessionController.createVote(id, voteRequest);
        verify(voteService).create(id, vote);
    }
}
