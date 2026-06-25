package br.ufrn.academix.framework.app.service;

import br.ufrn.academix.framework.app.api.dto.RegistroDTO;
import br.ufrn.academix.framework.app.api.dto.UsuarioPublicoDTO;
import br.ufrn.academix.framework.app.domain.model.AcademixProfile;
import br.ufrn.academix.framework.app.domain.repository.AcademixProfileRepository;
import br.ufrn.academix.framework.core.auth.FrameworkAccount;
import br.ufrn.academix.framework.core.auth.FrameworkAccountRepository;
import br.ufrn.academix.framework.core.files.DocumentProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AcademixProfileService {

    private final AcademixProfileRepository profileRepository;
    private final FrameworkAccountRepository accountRepository;
    private final DocumentProcessor documentProcessor;
    private final EmailService emailService;

    public AcademixProfileService(AcademixProfileRepository profileRepository, 
                                  FrameworkAccountRepository accountRepository,
                                  DocumentProcessor documentProcessor,
                                  EmailService emailService) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
        this.documentProcessor = documentProcessor;
        this.emailService = emailService;
    }

    public AcademixProfile registrar(RegistroDTO dto) {
        if (accountRepository.findAll().stream().anyMatch(a -> a.getEmail().equals(dto.email()))) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        // 1. Cria a Conta no Framework
        FrameworkAccount account = new FrameworkAccount();
        account.setName(dto.nome());
        account.setEmail(dto.email());
        account.setPassword(dto.senha());

        // 2. Cria o Perfil no App e faz a composição
        AcademixProfile profile = new AcademixProfile();
        profile.setAccount(account);
        profile.setLattesId(dto.lattesId());
        profile.setAcademicLevel(dto.academicLevel());

        AcademixProfile salvo = profileRepository.save(profile);
        emailService.enviarEmailBoasVindas(salvo.getAccount().getEmail(), salvo.getAccount().getName());
        return salvo;
    }

    public Optional<AcademixProfile> autenticar(String email, String senha) {
        return profileRepository.findByAccountEmail(email)
                .filter(p -> p.getAccount().getPassword().equals(senha));
    }

    public void salvarCurriculoXml(UUID profileId, MultipartFile arquivo) {
        AcademixProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado."));

        // O Framework extrai o texto do XML automaticamente!
        String conteudo = documentProcessor.processFile(arquivo);
        
        profile.setCurriculoTexto(conteudo);
        profileRepository.save(profile);
        emailService.enviarEmailAtualizacaoCurriculo(profile.getAccount().getEmail(), profile.getAccount().getName());
    }

    public String buscarCurriculo(UUID profileId) {
        return profileRepository.findById(profileId)
                .map(AcademixProfile::getCurriculoTexto)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado."));
    }

    public String buscarNomeUsuario(UUID profileId) {
        return profileRepository.findById(profileId)
                .map(p -> p.getAccount().getName())
                .orElse("Pesquisador");
    }

    public List<UsuarioPublicoDTO> listarUsuariosPublicos() {
        return profileRepository.findAll().stream()
                .map(p -> {
                    String textoOriginal = p.getCurriculoTexto();
                    String resumo = "Pesquisador no Academix AI";

                    if (textoOriginal != null && !textoOriginal.isBlank()) {
                        String textoLimpo = textoOriginal.replaceAll("<[^>]*>", " ").trim();
                        resumo = textoLimpo.length() > 150 
                            ? textoLimpo.substring(0, 150) + "..." 
                            : textoLimpo;
                    }

                    return UsuarioPublicoDTO.builder()
                            .id(p.getId())
                            .accountId(p.getAccount().getId()) // <-- NOVO: Extraindo o ID da conta!
                            .nome(p.getAccount().getName())
                            .descricaoCurta(resumo)
                            .build();
                })
                .collect(Collectors.toList());
    }
}