package com.ruriel.assembly.api.v1.resources;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingSessionResponse {

    private Long id;
    private Long agendaId;

    private Date createdAt;

    private Date startsAt;

    private Date endsAt;

    private Long totalVotes;

    private Long yesVotes;

    private Long noVotes;

    private String result;
}
