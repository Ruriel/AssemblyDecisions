package com.ruriel.assembly.api.v1.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaRequest {
    @NotBlank(message = "Name should not be null or blank.")
    @Size(max = 30, message = "Name must have a maximum of 30 characters.")
    private String name;

    @NotBlank(message = "Description should not be null or blank.")
    private String description;

    private Set<Long> associateIds = new HashSet<>();

}
