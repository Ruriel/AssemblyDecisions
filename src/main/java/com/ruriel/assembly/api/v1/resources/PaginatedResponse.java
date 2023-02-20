package com.ruriel.assembly.api.v1.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginatedResponse<T> {
    private int number;
    private int size;
    private int totalPages;
    private List<T> content;

}
