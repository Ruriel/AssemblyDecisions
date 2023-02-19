package com.ruriel.assembly.api.v1.controllers;

import com.ruriel.assembly.api.v1.resources.AgendaRequest;
import com.ruriel.assembly.api.v1.resources.AgendaResponse;
import com.ruriel.assembly.api.v1.resources.PaginatedResponse;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.services.AgendaService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/agenda", produces = "application/vnd.assembly.api.v1+json")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> findPage(@PageableDefault(sort = {"createdAt"}) Pageable pageable) {
        var page = agendaService.findPage(pageable);
        var typeToken = new TypeToken<PaginatedResponse<AgendaResponse>>() {
        }.getType();
        var agendaPaginatedResponse = modelMapper.map(page, typeToken);
        return ResponseEntity.ok(agendaPaginatedResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        var agenda = agendaService.findById(id);
        var agendaResponse = modelMapper.map(agenda, AgendaResponse.class);
        return ResponseEntity.ok(agendaResponse);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid AgendaRequest agendaRequest) {
        var agenda = modelMapper.map(agendaRequest, Agenda.class);
        var savedAgenda = agendaService.create(agenda);
        var responseBody = modelMapper.map(savedAgenda, AgendaResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid AgendaRequest agendaRequest) {
        var agenda = modelMapper.map(agendaRequest, Agenda.class);
        agendaService.update(id, agenda);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        agendaService.disable(id);
        return ResponseEntity.ok().build();
    }
}
