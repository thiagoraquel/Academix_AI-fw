package br.ufrn.academix.framework.app.service;

import br.ufrn.academix.framework.app.api.dto.PublicacaoPorTipoDTO;
import br.ufrn.academix.framework.app.domain.repository.ArtigoOpenAlexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtigoService {

    private final ArtigoOpenAlexRepository repository;

    public ArtigoService(ArtigoOpenAlexRepository repository) {
        this.repository = repository;
    }

    public List<PublicacaoPorTipoDTO> getDistribuicaoPorTipo() {
        List<Object[]> resultadosBanco = repository.contarPublicacoesPorTipo();

        return resultadosBanco.stream()
                .map(linha -> {
                    String tipo = (String) linha[0];
                    Long quantidade = ((Number) linha[1]).longValue();
                    return PublicacaoPorTipoDTO.builder().tipo(tipo).quantidade(quantidade).build();
                })
                .toList(); 
    }
}