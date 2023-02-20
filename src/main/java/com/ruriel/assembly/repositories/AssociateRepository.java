package com.ruriel.assembly.repositories;

import com.ruriel.assembly.entities.Associate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, Long> {
    Page<Associate> findByEnabled(Boolean enabled, Pageable pageable);

}
