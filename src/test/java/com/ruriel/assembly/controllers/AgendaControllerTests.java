package com.ruriel.assembly.controllers;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.api.v1.controllers.AgendaController;
import com.ruriel.assembly.api.v1.resources.AgendaDetailedResponse;
import com.ruriel.assembly.api.v1.resources.AgendaRequest;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.services.AgendaService;
import com.ruriel.assembly.services.AssociateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaControllerTests {
    @Mock
    private AgendaService agendaService;

    @Mock
    private AssociateService associateService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AgendaController agendaController;

    @Test
    void shouldGetAPage() {
        var pageable = PageRequest.of(0, 5);
        agendaController.findPage(pageable);
        verify(agendaService).findPage(pageable);
    }

    @Test
    void shouldFindById() {
        var id = 1L;
        var agenda = Agenda.builder().id(1L).build();
        when(agendaService.findById(id)).thenReturn(agenda);
        agendaController.findById(id);
        verify(agendaService).findById(id);
    }

    @Test
    void shouldCreate() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).associates(new HashSet<>()).build();
        var agendaRequest = new AgendaRequest();
        when(associateService.findAllById(anySet())).thenReturn(new HashSet<>());
        when(modelMapper.map(agendaRequest, Agenda.class)).thenReturn(agenda);
        agendaController.create(agendaRequest);
        verify(agendaService).create(agenda);
    }

    @Test
    void shouldUpdate() {
        var id = 1L;
        var agenda = Agenda.builder().id(id).associates(new HashSet<>()).build();
        var agendaRequest = new AgendaRequest();
        when(associateService.findAllById(anySet())).thenReturn(new HashSet<>());
        when(modelMapper.map(agendaRequest, Agenda.class)).thenReturn(agenda);
        agendaController.update(id, agendaRequest);
        verify(agendaService).update(id, agenda);
    }

    @Test
    void shouldDisable() {
        var id = 1L;
        agendaController.disable(id);
        verify(agendaService).disable(id);
    }
}
