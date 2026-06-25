package br.ufrn.academix.framework.app.api.controller;

import br.ufrn.academix.framework.app.api.dto.GeneroPorcentagemDTO;
import br.ufrn.academix.framework.app.service.EstatisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estatisticas")
@CrossOrigin(origins = "http://localhost:3000")
public class EstatisticaController {

    private final EstatisticaService service;

    public EstatisticaController(EstatisticaService service) {
        this.service = service;
    }

    @GetMapping("/genero")
    public ResponseEntity<List<GeneroPorcentagemDTO>> getPorcentagemGenero() {
        List<GeneroPorcentagemDTO> dados = service.calcularPorcentagemPorGenero();
        
        if (dados == null || dados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(dados);
    }
}