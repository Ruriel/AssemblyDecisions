package com.ruriel.assembly.api.v1.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgendaDetailedResponse {
    private Long id;

    private String name;

    private String description;
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<AssociateResponse> associates;
}
