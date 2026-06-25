package br.ufrn.academix.framework.app.api.controller;

import br.ufrn.academix.framework.app.api.dto.ComparacaoUniversidadeDTO;
import br.ufrn.academix.framework.app.service.ComparacaoUniversidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comparacao")
@CrossOrigin(origins = "http://localhost:3000")
public class ComparacaoController {

    private final ComparacaoUniversidadeService service;

    public ComparacaoController(ComparacaoUniversidadeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ComparacaoUniversidadeDTO> getComparacao(@RequestParam String universidade) {
        ComparacaoUniversidadeDTO dados = service.obterDadosComparacao(universidade);
        
        // Aplica a regra de negócio do No Content (204)
        if (dados.totalArtigos() == 0 && dados.totalFormados() == 0) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/sugestoes")
    public ResponseEntity<List<String>> buscarSugestoes(@RequestParam String termo) {
        List<String> sugestoes = service.buscarSugestoes(termo);
        return ResponseEntity.ok(sugestoes);
    }
}