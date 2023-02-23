package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.repositories.AgendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTests {
    @Mock
    private AgendaRepository agendaRepository;
    @InjectMocks
    private AgendaService agendaService;

    @Test
    void shouldFindPage(){
        var pageable = PageRequest.of(0, 5);
        agendaService.findPage(pageable);
        verify(agendaRepository).findByEnabled(true, pageable);
    }

    @Test
    void shouldCreate(){
        var agenda = Agenda.builder().name("Testing").description("Testing").build();
        agendaService.create(agenda);
        verify(agendaRepository).save(agenda);
    }

    @Test
    void shouldFindById(){
        var id = 1L;
        var agenda = Agenda.builder().id(id).build();
        when(agendaRepository.findById(id)).thenReturn(Optional.of(agenda));
        agendaService.findById(id);
        verify(agendaRepository).findById(id);
    }

    @Test
    void shouldNotFindById(){
        var id = 1L;
        assertThatThrownBy(() -> agendaService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdate(){
        var id = 1L;
        var currentAgenda = Agenda.builder().id(id).build();
        var agenda = Agenda.builder().name("New name").build();
        when(agendaRepository.findById(id)).thenReturn(Optional.of(currentAgenda));
        agendaService.update(1L, agenda);
        verify(agendaRepository).save(currentAgenda);
    }

    @Test
    void shouldDisable(){
        var id = 1L;
        var currentAgenda = Agenda.builder().id(id).enabled(true).build();
        when(agendaRepository.findById(id)).thenReturn(Optional.of(currentAgenda));
        agendaService.disable(1L);
        verify(agendaRepository).save(currentAgenda);
    }
}
