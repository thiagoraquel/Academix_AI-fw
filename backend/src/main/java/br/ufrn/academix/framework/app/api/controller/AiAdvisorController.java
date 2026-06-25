package br.ufrn.academix.framework.app.api.controller;

import br.ufrn.academix.framework.app.ai.AconselhamentoMestradoTask;
import br.ufrn.academix.framework.core.files.DocumentProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:3000")
public class AiAdvisorController {

    private final AconselhamentoMestradoTask conselhoTask;
    private final DocumentProcessor documentProcessor;

    public AiAdvisorController(AconselhamentoMestradoTask conselhoTask, DocumentProcessor documentProcessor) {
        this.conselhoTask = conselhoTask;
        this.documentProcessor = documentProcessor;
    }

    // Rota 1: Para o botão "Conselho Rápido" (Recebe JSON puro)
    @PostMapping("/conselho/rapido/{accountId}")
    public ResponseEntity<String> conselhoRapido(
            @PathVariable UUID accountId,
            @RequestBody Map<String, String> payload) {
        try {
            String mensagem = payload.get("mensagem");
            
            // Passamos null para o documento, o Template ignora a injeção do arquivo
            String respostaIA = conselhoTask.executeTask(accountId, null, mensagem);
            
            return ResponseEntity.ok(respostaIA);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }

    // Rota 2: Para o Chat com ou sem PDF (Recebe FormData)
    @PostMapping("/conselho/{accountId}")
    public ResponseEntity<String> conselhoComArquivo(
            @PathVariable UUID accountId,
            @RequestParam("mensagem") String mensagem,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            String documentText = "";
            
            // O Motor do Framework extrai o texto do PDF/TXT/XML automaticamente
            if (file != null && !file.isEmpty()) {
                documentText = documentProcessor.processFile(file);
            }
            
            String respostaIA = conselhoTask.executeTask(accountId, documentText, mensagem);
            
            return ResponseEntity.ok(respostaIA);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }
}