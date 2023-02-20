package com.ruriel.assembly.api.v1.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingSessionResponse {

    private Long id;
    private Long agendaId;

    private LocalDateTime createdAt;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private Long totalVotes;

    private Long yesVotes;

    private Long noVotes;

    private String result;
}
