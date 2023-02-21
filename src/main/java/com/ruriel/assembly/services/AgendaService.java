package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.repositories.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.ruriel.assembly.api.exceptions.messages.AgendaMessages.AGENDA_NOT_FOUND;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    public Page<Agenda> findPage(Pageable pageable) {
        return agendaRepository.findByEnabled(true, pageable);
    }

    public Set<Agenda> findAllById(Set<Long> ids){
        return new HashSet<>(agendaRepository.findAllById(ids));
    }
    public Agenda create(Agenda agenda) {
        var now = LocalDateTime.now();
        agenda.setEnabled(true);
        agenda.setCreatedAt(now);
        return agendaRepository.save(agenda);
    }

    public Agenda findById(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(AGENDA_NOT_FOUND, id)));
    }

    public Agenda update(Long id, Agenda agenda) {
        var now = LocalDateTime.now();
        var current = findById(id);
        current.setDescription(agenda.getDescription());
        current.setName(agenda.getName());
        current.setUpdatedAt(now);
        current.setAssociates(agenda.getAssociates());
        return agendaRepository.save(current);

    }

    public Agenda disable(Long id) {
        var now = LocalDateTime.now();
        var current = findById(id);
        current.setEnabled(false);
        current.setUpdatedAt(now);
        return agendaRepository.save(current);
    }
}
