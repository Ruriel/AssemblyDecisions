package com.ruriel.assembly.controllers;

import com.ruriel.assembly.api.v1.controllers.AgendaController;
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
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
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
