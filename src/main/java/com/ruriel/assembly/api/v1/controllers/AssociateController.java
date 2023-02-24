package com.ruriel.assembly.api.v1.controllers;

import com.ruriel.assembly.api.v1.resources.*;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.services.AssociateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/associate", produces = "application/vnd.assembly.api.v1+json")
@RequiredArgsConstructor
@Slf4j
public class AssociateController {

    private final AssociateService associateService;

    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<PaginatedResponse<AssociateResponse>> findPage(@PageableDefault(sort = {"createdAt"}) Pageable pageable) {
        log.trace("GET /associate");
        log.trace(pageable.toString());
        var page = associateService.findPage(pageable);
        var typeToken = new TypeToken<PaginatedResponse<AssociateResponse>>() {
        }.getType();
        PaginatedResponse<AssociateResponse> associatePaginatedResponse = modelMapper.map(page, typeToken);
        return ResponseEntity.ok(associatePaginatedResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<AssociateDetailedResponse> findById(@PathVariable Long id) {
        log.trace("GET /associate/" + id);
        var associate = associateService.findById(id);
        var associateResponse = modelMapper.map(associate, AssociateDetailedResponse.class);
        return ResponseEntity.ok(associateResponse);
    }

    @PostMapping
    public ResponseEntity<AssociateDetailedResponse> create(@RequestBody @Valid AssociateRequest associateRequest) {
        log.trace("POST /associate: "+ associateRequest.toString());
        var associate = modelMapper.map(associateRequest, Associate.class);
        var savedAssociate = associateService.create(associate);
        var responseBody = modelMapper.map(savedAssociate, AssociateDetailedResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssociateDetailedResponse> update(@PathVariable Long id, @RequestBody @Valid AssociateRequest associateRequest) {
        log.trace(String.format("PUT /associate/%d: %s", id, associateRequest.toString()));
        var associate = modelMapper.map(associateRequest, Associate.class);
        var updatedAssociate = associateService.update(id, associate);
        var responseBody = modelMapper.map(updatedAssociate, AssociateDetailedResponse.class);
        return ResponseEntity.ok(responseBody);
    }

}
