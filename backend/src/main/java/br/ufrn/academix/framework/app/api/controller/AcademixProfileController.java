package br.ufrn.academix.framework.app.api.controller;

import br.ufrn.academix.framework.app.api.dto.LoginDTO;
import br.ufrn.academix.framework.app.api.dto.RegistroDTO;
import br.ufrn.academix.framework.app.api.dto.UsuarioPublicoDTO;
import br.ufrn.academix.framework.app.domain.model.AcademixProfile;
import br.ufrn.academix.framework.app.service.AcademixProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:3000")
public class AcademixProfileController {

    private final AcademixProfileService service;

    public AcademixProfileController(AcademixProfileService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        return service.autenticar(login.email(), login.senha())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroDTO dto) {
        try {
            AcademixProfile salvo = service.registrar(dto);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/upload-curriculo")
    public ResponseEntity<String> upload(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            service.salvarCurriculoXml(id, file);
            return ResponseEntity.ok("Currículo atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/curriculo")
    public ResponseEntity<String> getCurriculo(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarCurriculo(id));
    }

    @GetMapping("/explorar")
    public ResponseEntity<List<UsuarioPublicoDTO>> explorarUsuarios() {
        return ResponseEntity.ok(service.listarUsuariosPublicos());
    }

    @GetMapping("/{id}/nome")
    public ResponseEntity<String> getNome(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarNomeUsuario(id));
    }
}