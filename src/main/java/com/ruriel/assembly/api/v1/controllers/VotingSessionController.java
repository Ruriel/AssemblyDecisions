package com.ruriel.assembly.api.v1.controllers;

import com.ruriel.assembly.api.v1.resources.*;
import com.ruriel.assembly.entities.Vote;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.services.VoteService;
import com.ruriel.assembly.services.VotingSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/voting-session", produces = "application/vnd.assembly.api.v1+json")
@RequiredArgsConstructor
@Slf4j
public class VotingSessionController {
    private final VoteService voteService;
    private final VotingSessionService votingSessionService;

    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<PaginatedResponse<VotingSessionResponse>> findPage(@PageableDefault(sort = {"startsAt"}) Pageable pageable) {
        log.trace("GET /voting-session");
        log.trace(pageable.toString());
        var page = votingSessionService.findPage(pageable);
        var typeToken = new TypeToken<PaginatedResponse<VotingSessionResponse>>() {
        }.getType();
        PaginatedResponse<VotingSessionResponse> votingSessionPaginatedResponse = modelMapper.map(page, typeToken);
        return ResponseEntity.status(HttpStatus.OK).body(votingSessionPaginatedResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<VotingSessionResponse> findById(@PathVariable Long id) {
        log.trace("GET /voting-session/" + id);
        var votingSession = votingSessionService.findById(id);
        var votingSessionResponse = modelMapper.map(votingSession, VotingSessionResponse.class);
        return ResponseEntity.ok(votingSessionResponse);
    }

    @PostMapping
    public ResponseEntity<VotingSessionResponse> create(@RequestBody @Valid VotingSessionRequest votingSessionRequest){
        log.trace("POST /voting-session: "+ votingSessionRequest.toString());
        var votingSession = modelMapper.map(votingSessionRequest, VotingSession.class);
        var savedVotingSession = votingSessionService.create(votingSession);
        var responseBody = modelMapper.map(savedVotingSession, VotingSessionResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VotingSessionResponse> update(@PathVariable Long id, @RequestBody @Valid VotingSessionPatchRequest votingSessionPatchRequest){
        log.trace("PATCH /voting-session: "+ votingSessionPatchRequest);
        var votingSession = modelMapper.map(votingSessionPatchRequest, VotingSession.class);
        var updatedVotingSession = votingSessionService.update(id, votingSession);
        var responseBody = modelMapper.map(updatedVotingSession, VotingSessionResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
    @PostMapping("/{id}/vote")
    public ResponseEntity<VoteResponse> createVote(@PathVariable Long id, @RequestBody @Valid VoteRequest voteRequest) {
        log.trace(String.format("POST /voting-session/%d/vote: %s", id, voteRequest));
        var vote = modelMapper.map(voteRequest, Vote.class);
        var savedVote = voteService.create(id, vote);
        var responseBody = modelMapper.map(savedVote, VoteResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

}
