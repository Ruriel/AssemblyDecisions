package com.ruriel.assembly.api.v1.resources;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotePatchRequest {

    @NotNull(message = "Vote must have a content. Either true or false.")
    private Boolean content;

}
