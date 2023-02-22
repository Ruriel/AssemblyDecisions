package com.ruriel.assembly.api.v1.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociateRequest {
    @NotBlank(message = "Name should not be null or blank.")
    @Size(max = 30, message = "Name must have a maximum of 30 characters.")
    private String name;

    @NotBlank(message = "Description should not be null or blank.")
    @Size(min = 11, max = 11, message = "Document must have exactly 11 characters.")
    @Pattern(regexp = "^([0-9]{11})$", message = "Document should consist of 11 numbers from 0 to 9.")
    private String document;

    private Set<Long> agendaIds = new HashSet<>();
}
