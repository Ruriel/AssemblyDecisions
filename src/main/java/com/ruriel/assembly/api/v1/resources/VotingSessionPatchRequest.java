package com.ruriel.assembly.api.v1.resources;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingSessionPatchRequest {
    @NotNull(message = "Must have a startsAt date.")
    private Date startsAt;

    private Date endsAt;
}