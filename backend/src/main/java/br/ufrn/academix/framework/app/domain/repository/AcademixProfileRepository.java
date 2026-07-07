package br.ufrn.academix.framework.app.domain.repository;

import br.ufrn.academix.framework.app.domain.model.AcademixProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AcademixProfileRepository extends JpaRepository<AcademixProfile, UUID> {
    // Como a conta é outra tabela, usamos a navegação do JPA para buscar o perfil pelo email da conta
    Optional<AcademixProfile> findByAccountEmail(String email);

    // Usado na IA para buscar o perfil pelo ID da conta do framework
    Optional<AcademixProfile> findByAccountId(UUID accountId);
}