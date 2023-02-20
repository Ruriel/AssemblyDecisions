package com.ruriel.assembly.api.v1.resources;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociateResponse {
    private Long id;

    private String name;

    private String document;

    private Date createdAt;

    private Date updatedAt;
}
