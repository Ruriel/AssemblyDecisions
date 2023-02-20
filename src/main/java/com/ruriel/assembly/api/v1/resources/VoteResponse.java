package com.ruriel.assembly.api.v1.resources;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {

    private Long id;
    private Long associateId;

    private Boolean content;

    private Long votingSessionId;

    private Date createdAt;

    private Date updatedAt;
}
