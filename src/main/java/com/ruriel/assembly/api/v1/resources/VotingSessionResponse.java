package com.ruriel.assembly.api.v1.resources;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingSessionRequest {
    @NotBlank
    private Long agendaId;

    @NotBlank
    private Date startsAt;

    private Date endsAt;
}
