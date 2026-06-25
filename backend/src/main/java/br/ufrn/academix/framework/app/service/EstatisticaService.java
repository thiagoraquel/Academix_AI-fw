package br.ufrn.academix.framework.app.service;

import br.ufrn.academix.framework.app.api.dto.GeneroPorcentagemDTO;
import br.ufrn.academix.framework.app.domain.repository.LattesPainelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstatisticaService {

    private final LattesPainelRepository repository;

    public EstatisticaService(LattesPainelRepository repository) {
        this.repository = repository;
    }

    public List<GeneroPorcentagemDTO> calcularPorcentagemPorGenero() {
        List<Object[]> resultadosBanco = repository.contarRegistrosPorSexo();
        
        double totalGeral = 0;
        for (Object[] linha : resultadosBanco) {
            totalGeral += ((Number) linha[1]).doubleValue();
        }

        List<GeneroPorcentagemDTO> porcentagens = new ArrayList<>();
        for (Object[] linha : resultadosBanco) {
            String genero = (String) linha[0];
            double quantidade = ((Number) linha[1]).doubleValue();
            
            double porcentagem = (quantidade / totalGeral) * 100;
            porcentagem = Math.round(porcentagem * 100.0) / 100.0; 
            
            porcentagens.add(GeneroPorcentagemDTO.builder().genero(genero).porcentagem(porcentagem).build());
        }

        return porcentagens;
    }
}