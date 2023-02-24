package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.entities.Associate;
import com.ruriel.assembly.repositories.AssociateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.ruriel.assembly.api.exceptions.messages.AssociateMessages.ASSOCIATE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AssociateService {
    private final AssociateRepository associateRepository;

    public Page<Associate> findPage(Pageable pageable) {
        return associateRepository.findAll(pageable);
    }

    public Set<Associate> findAllById(Set<Long> ids) {
        return new HashSet<>(associateRepository.findAllById(ids));
    }

    public Associate create(Associate associate) {
        var now = LocalDateTime.now();
        associate.setEnabled(true);
        associate.setCreatedAt(now);
        return associateRepository.save(associate);
    }

    public Associate findById(Long id) {
        return associateRepository.findById(id)
                .orElseThrow(() -> {
                    var message = String.format(ASSOCIATE_NOT_FOUND, id);
                    return new ResourceNotFoundException(message);
                });
    }

    public Associate update(Long id, Associate associate) {
        var now = LocalDateTime.now();
        var current = findById(id);
        current.setName(associate.getName());
        current.setDocument(associate.getDocument());
        current.setUpdatedAt(now);
        return associateRepository.save(current);
    }

}
