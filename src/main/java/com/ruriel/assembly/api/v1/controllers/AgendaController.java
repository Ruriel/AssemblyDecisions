package com.ruriel.assembly.api.v1.controllers;

import com.ruriel.assembly.api.v1.resources.AgendaRequest;
import com.ruriel.assembly.api.v1.resources.AgendaDetailedResponse;
import com.ruriel.assembly.api.v1.resources.AgendaResponse;
import com.ruriel.assembly.api.v1.resources.PaginatedResponse;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.services.AgendaService;
import com.ruriel.assembly.services.AssociateService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/agenda", produces = "application/vnd.assembly.api.v1+json")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @Autowired
    private AssociateService associateService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<PaginatedResponse<AgendaResponse>> findPage(@PageableDefault(sort = {"createdAt"}) Pageable pageable) {
        var page = agendaService.findPage(pageable);
        var typeToken = new TypeToken<PaginatedResponse<AgendaResponse>>() {
        }.getType();
        PaginatedResponse<AgendaResponse> agendaResponse = modelMapper.map(page, typeToken);
        return ResponseEntity.ok(agendaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaDetailedResponse> findById(@PathVariable Long id) {
        var agenda = agendaService.findById(id);
        var agendaResponse = modelMapper.map(agenda, AgendaDetailedResponse.class);
        return ResponseEntity.ok(agendaResponse);
    }

    @PostMapping
    public ResponseEntity<AgendaDetailedResponse> create(@RequestBody @Valid AgendaRequest agendaRequest) {
        var associates = associateService.findAllById(agendaRequest.getAssociates());
        var agenda = modelMapper.map(agendaRequest, Agenda.class);
        agenda.setAssociates(associates);
        var savedAgenda = agendaService.create(agenda);
        var responseBody = modelMapper.map(savedAgenda, AgendaDetailedResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendaDetailedResponse> update(@PathVariable Long id, @RequestBody @Valid AgendaRequest agendaRequest) {
        var associates = associateService.findAllById(agendaRequest.getAssociates());
        var agenda = modelMapper.map(agendaRequest, Agenda.class);
        agenda.setAssociates(associates);
        var updatedAgenda = agendaService.update(id, agenda);
        var responseBody = modelMapper.map(updatedAgenda, AgendaDetailedResponse.class);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AgendaDetailedResponse> disable(@PathVariable Long id) {
        var agenda = agendaService.disable(id);
        var responseBody = modelMapper.map(agenda, AgendaDetailedResponse.class);
        return ResponseEntity.ok(responseBody);
    }
}
