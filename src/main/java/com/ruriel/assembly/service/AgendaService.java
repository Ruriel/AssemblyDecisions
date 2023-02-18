package com.ruriel.assembly.service;

import com.ruriel.assembly.entity.Agenda;
import com.ruriel.assembly.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    public Page<Agenda> findPage(Pageable pageable){
        return agendaRepository.findAll(pageable);
    }
    public Agenda create(Agenda agenda){
        var now = Date.from(Instant.now());
        agenda.setEnabled(true);
        agenda.setVotingSessions(new HashSet<>());
        agenda.setCreatedAt(now);
        return agendaRepository.save(agenda);
    }

    public Optional<Agenda> findById(Long id){
        return agendaRepository.findById(id);
    }
    public Boolean update(Long id, Agenda agenda){
        return agendaRepository.findById(id).map(current -> {
            var now = Date.from(Instant.now());
            current.setDescription(agenda.getDescription());
            current.setName(agenda.getName());
            current.setUpdatedAt(now);
            agendaRepository.save(current);
            return true;
        }).orElse(false);
    }

    public Boolean disable(Long id){
        return agendaRepository.findById(id).map(current -> {
            var now = Date.from(Instant.now());
            current.setEnabled(false);
            current.setUpdatedAt(now);
            agendaRepository.save(current);
            return true;
        }).orElse(false);
    }
}
