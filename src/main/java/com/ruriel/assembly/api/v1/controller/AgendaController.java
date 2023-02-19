package com.ruriel.assembly.api.v1.controller;

import com.ruriel.assembly.api.v1.resource.AgendaPaginatedResponse;
import com.ruriel.assembly.api.v1.resource.AgendaRequest;
import com.ruriel.assembly.api.v1.resource.AgendaResponse;
import com.ruriel.assembly.entity.Agenda;
import com.ruriel.assembly.service.AgendaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/agenda")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> findPage(@PageableDefault(sort = {"createdAt"}) Pageable pageable){
        var page = agendaService.findPage(pageable);
        var agendaPaginatedResponse = modelMapper.map(page, AgendaPaginatedResponse.class);
        return ResponseEntity.ok(agendaPaginatedResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return agendaService.findById(id)
                .map(agenda -> {
                    var agendaResponse = modelMapper.map(agenda, AgendaResponse.class);
                    return ResponseEntity.ok(agendaResponse);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AgendaRequest agendaRequest){
        var agenda = modelMapper.map(agendaRequest, Agenda.class);
        var savedAgenda = agendaService.create(agenda);
        var responseBody = modelMapper.map(savedAgenda, AgendaResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AgendaRequest agendaRequest){
        var agenda = modelMapper.map(agendaRequest, Agenda.class);
        return agendaService.update(id, agenda) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id){
        return agendaService.disable(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
