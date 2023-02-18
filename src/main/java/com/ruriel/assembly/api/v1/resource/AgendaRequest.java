package com.ruriel.assembly.api.v1.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaRequest {
    @NotBlank
    @Size(max = 30, message = "Agenda name must have 30 characters at most")
    private String name;

    @NotBlank
    private String description;

}
