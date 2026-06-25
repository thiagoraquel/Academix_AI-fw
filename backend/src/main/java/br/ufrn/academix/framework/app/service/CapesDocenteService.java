package br.ufrn.academix.framework.app.service;

import br.ufrn.academix.framework.app.api.dto.AreaConhecimentoDTO;
import br.ufrn.academix.framework.app.domain.repository.CapesDocenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CapesDocenteService {

    private final CapesDocenteRepository repository;

    public CapesDocenteService(CapesDocenteRepository repository) {
        this.repository = repository;
    }

    public List<AreaConhecimentoDTO> getTopAreasConhecimento() {
        List<Object[]> resultadosBanco = repository.contarDocentesPorArea();

        return resultadosBanco.stream()
                .limit(8)
                .map(linha -> {
                    String area = (String) linha[0];
                    Long quantidade = ((Number) linha[1]).longValue();
                    return AreaConhecimentoDTO.builder().area(area).quantidade(quantidade).build();
                })
                .toList();
    }
}