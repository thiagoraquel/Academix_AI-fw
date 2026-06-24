package br.ufrn.academix.framework.app.domain;

import br.ufrn.academix.framework.core.auth.BusinessIdentifierProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AcademixIdentifierProvider implements BusinessIdentifierProvider {

    private final AcademixProfileRepository repository;

    public AcademixIdentifierProvider(AcademixProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getIdentifierForAccount(UUID accountId) {
        // Busca o perfil da aplicação que pertence àquela conta do framework
        return repository.findAll().stream()
                .filter(profile -> profile.getAccount().getId().equals(accountId))
                .map(AcademixProfile::getLattesId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado para a conta informada."));
    }
}