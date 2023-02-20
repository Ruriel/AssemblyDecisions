package com.ruriel.assembly.api.v1.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponse {
    private Long id;

    private String name;

    private String description;
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
