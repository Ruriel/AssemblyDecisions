package com.ruriel.assembly.api.v1.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgendaPaginatedResponse {
    private int number;
    private int size;
    private int totalPages;
    private List<AgendaResponse> content;

}
