package br.ufrn.academix.framework.app.service;

import br.ufrn.academix.framework.app.api.dto.InstituicaoTopDTO;
import br.ufrn.academix.framework.app.domain.model.InstituicaoOpenAlex;
import br.ufrn.academix.framework.app.domain.repository.InstituicaoOpenAlexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituicaoService {

    private final InstituicaoOpenAlexRepository repository;

    public InstituicaoService(InstituicaoOpenAlexRepository repository) {
        this.repository = repository;
    }

    public List<InstituicaoTopDTO> getTop10Instituicoes() {
        List<InstituicaoOpenAlex> topInstituicoes = repository.findTop10ByOrderByWorksCountDesc();

        return topInstituicoes.stream()
                .map(inst -> InstituicaoTopDTO.builder()
                        .nome(inst.getDisplayName())
                        .publicacoes(inst.getWorksCount())
                        .build())
                .toList();
    }
}