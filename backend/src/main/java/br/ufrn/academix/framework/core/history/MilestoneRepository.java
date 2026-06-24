package br.ufrn.academix.framework.core.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {
    
    // Busca todas as conquistas de uma conta base e já devolve ordenado do mais recente pro mais antigo
    List<Milestone> findByAccountIdOrderByReferenceYearDesc(UUID accountId);
}