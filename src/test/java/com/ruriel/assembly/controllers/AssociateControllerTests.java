package com.ruriel.assembly.controllers;

import com.ruriel.assembly.api.v1.controllers.AssociateController;
import com.ruriel.assembly.api.v1.resources.AssociateRequest;
import com.ruriel.assembly.entities.Associate;
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
class AssociateControllerTests {

    @Mock
    private AssociateService associateService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AssociateController associateController;

    @Test
    void shouldGetAPage() {
        var pageable = PageRequest.of(0, 5);
        associateController.findPage(pageable);
        verify(associateService).findPage(pageable);
    }

    @Test
    void shouldFindById() {
        var id = 1L;
        associateController.findById(id);
        verify(associateService).findById(id);
    }

    @Test
    void shouldCreate() {
        var id = 1L;
        var associate = Associate.builder().id(id).agendas(new HashSet<>()).build();
        var associateRequest = new AssociateRequest();
        when(modelMapper.map(associateRequest, Associate.class)).thenReturn(associate);
        associateController.create(associateRequest);
        verify(associateService).create(associate);
    }

    @Test
    void shouldUpdate() {
        var id = 1L;
        var associate = Associate.builder().id(id).agendas(new HashSet<>()).build();
        var associateRequest = new AssociateRequest();
        when(modelMapper.map(associateRequest, Associate.class)).thenReturn(associate);
        associateController.update(id, associateRequest);
        verify(associateService).update(id, associate);
    }

}
