package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.repositories.AssociateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssociateServiceTests {
    @Mock
    private AssociateRepository associateRepository;
    @InjectMocks
    private AssociateService associateService;

    @Test
    void shouldFindPage(){
        var pageable = PageRequest.of(0, 5);
        associateService.findPage(pageable);
        verify(associateRepository).findAll(pageable);
    }

    @Test
    void shouldFindAllById(){
        var ids = Set.of(1L, 2L, 3L);
        associateService.findAllById(ids);
        verify(associateRepository).findAllById(ids);
    }

    @Test
    void shouldCreate(){
        var associate = Associate.builder().name("Testing").build();
        associateService.create(associate);
        verify(associateRepository).save(associate);
    }

    @Test
    void shouldFindById(){
        var id = 1L;
        var associate = Associate.builder().id(id).build();
        when(associateRepository.findById(id)).thenReturn(Optional.of(associate));
        associateService.findById(id);
        verify(associateRepository).findById(id);
    }

    @Test
    void shouldNotFindById(){
        var id = 1L;
        assertThatThrownBy(() -> associateService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdate(){
        var id = 1L;
        var currentAssociate = Associate.builder().id(id).build();
        var associate = Associate.builder().name("New name").build();
        when(associateRepository.findById(id)).thenReturn(Optional.of(currentAssociate));
        associateService.update(id, associate);
        verify(associateRepository).save(currentAssociate);
    }

}
