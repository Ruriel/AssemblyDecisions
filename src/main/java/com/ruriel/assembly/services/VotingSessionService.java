package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.repositories.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

@Service
public class AssociateService {
    @Autowired
    private AssociateRepository associateRepository;

    private final String ASSOCIATE_NOT_FOUND = "No associate with id %d found.";
    public Page<Associate> findPage(Pageable pageable) {
        return associateRepository.findByEnabled(true, pageable);
    }

    public Associate create(Associate associate) {
        associate.setEnabled(true);
        return associateRepository.save(associate);
    }

    public Associate findById(Long id) {
        return associateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ASSOCIATE_NOT_FOUND, id)));
    }

    public Associate update(Long id, Associate associate) {
        return associateRepository.findById(id).map(current -> {
            current.setName(associate.getName());
            current.setDocument(associate.getDocument());
            return associateRepository.save(current);
        }).orElseThrow(() -> new ResourceNotFoundException(String.format(ASSOCIATE_NOT_FOUND, id)));

    }

    public Associate disable(Long id) {
        return associateRepository.findById(id).map(current -> {
            current.setEnabled(false);
            return associateRepository.save(current);
        }).orElseThrow(() -> new ResourceNotFoundException(String.format(ASSOCIATE_NOT_FOUND, id)));
    }
}
