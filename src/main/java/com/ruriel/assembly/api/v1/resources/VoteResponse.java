package com.ruriel.assembly.api.v1.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {

    private Long id;
    private Long associateId;

    private Boolean content;

    private Long votingSessionId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
