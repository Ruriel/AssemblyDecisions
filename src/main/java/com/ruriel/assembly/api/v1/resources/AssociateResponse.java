package com.ruriel.assembly.api.v1.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociateResponse {
    private Long id;

    private String name;

    private String document;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
