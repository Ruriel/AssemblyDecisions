package com.ruriel.assembly.repository;

import com.ruriel.assembly.entity.Agenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    Page<Agenda> findByEnabled(Boolean enabled, Pageable pageable);
}
