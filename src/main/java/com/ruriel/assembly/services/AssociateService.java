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

import static com.ruriel.assembly.api.exceptions.messages.AssociateMessages.ASSOCIATE_NOT_FOUND;

@Service
public class AssociateService {
    @Autowired
    private AssociateRepository associateRepository;

    public Page<Associate> findPage(Pageable pageable) {
        return associateRepository.findByEnabled(true, pageable);
    }

    public Associate create(Associate associate) {
        associate.setEnabled(true);
        associate.setCreatedAt(Date.from(Instant.now()));
        return associateRepository.save(associate);
    }

    public Associate findById(Long id) {
        return associateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ASSOCIATE_NOT_FOUND, id)));
    }

    public Associate update(Long id, Associate associate) {
        var current = findById(id);
        current.setName(associate.getName());
        current.setDocument(associate.getDocument());
        current.setUpdatedAt(Date.from(Instant.now()));
        return associateRepository.save(current);
    }

    public Associate disable(Long id) {
        var current = findById(id);
        current.setEnabled(false);
        current.setUpdatedAt(Date.from(Instant.now()));
        return associateRepository.save(current);
    }
}
