package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.repositories.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

import static com.ruriel.assembly.api.exceptions.messages.AgendaMessages.AGENDA_NOT_FOUND;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    public Page<Agenda> findPage(Pageable pageable) {
        return agendaRepository.findByEnabled(true, pageable);
    }

    public Agenda create(Agenda agenda) {
        agenda.setEnabled(true);
        agenda.setCreatedAt(Date.from(Instant.now()));
        return agendaRepository.save(agenda);
    }

    public Agenda findById(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(AGENDA_NOT_FOUND, id)));
    }

    public Agenda update(Long id, Agenda agenda) {
        return agendaRepository.findById(id).map(current -> {
            current.setDescription(agenda.getDescription());
            current.setName(agenda.getName());
            current.setUpdatedAt(Date.from(Instant.now()));
            return agendaRepository.save(current);
        }).orElseThrow(() -> new ResourceNotFoundException(String.format(AGENDA_NOT_FOUND, id)));

    }

    public Agenda disable(Long id) {
        return agendaRepository.findById(id).map(current -> {
            current.setEnabled(false);
            current.setUpdatedAt(Date.from(Instant.now()));
            return agendaRepository.save(current);
        }).orElseThrow(() -> new ResourceNotFoundException(String.format(AGENDA_NOT_FOUND, id)));
    }
}
