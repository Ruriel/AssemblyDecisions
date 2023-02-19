package com.ruriel.assembly.repositories;

import com.ruriel.assembly.entities.Agenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    Page<Agenda> findByEnabled(Boolean enabled, Pageable pageable);
}
