package br.ufrn.academix.framework.app.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AcademixProfileRepository extends JpaRepository<AcademixProfile, UUID> {
}