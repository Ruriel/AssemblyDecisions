package com.ruriel.assembly.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaRequest {
    private String name;

    private String description;

    private Date createdAt;

    private Date updatedAt;
}
