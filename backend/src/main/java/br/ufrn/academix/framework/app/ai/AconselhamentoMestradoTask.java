package br.ufrn.academix.framework.app.ai;

import br.ufrn.academix.framework.app.domain.model.AcademixProfile;
import br.ufrn.academix.framework.app.domain.repository.AcademixProfileRepository;
import br.ufrn.academix.framework.core.ai.AiTaskTemplate;
import br.ufrn.academix.framework.core.ai.LlmClient;
import br.ufrn.academix.framework.core.history.MilestoneService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AconselhamentoMestradoTask extends AiTaskTemplate {

    private final AcademixProfileRepository profileRepository;

    // Repassamos os dois primeiros para o Framework (super) e guardamos o repositório para o App
    public AconselhamentoMestradoTask(
            LlmClient llmClient, 
            MilestoneService milestoneService,
            AcademixProfileRepository profileRepository) {
        super(llmClient, milestoneService);
        this.profileRepository = profileRepository;
    }

    @Override
    protected String getSystemPersona() {
        return "Você é o Conselheiro Acadêmico Inteligente do sistema Academix AI. " +
               "Seu tom deve ser encorajador, direto, profissional e focado em impulsionar a trajetória de pesquisa do estudante.";
    }

    @Override
    protected String extractProfileData(UUID accountId) {
        // A aplicação vai no banco, busca o currículo real atrelado à conta e devolve o texto
        return profileRepository.findByAccountId(accountId)
                .map(AcademixProfile::getCurriculoTexto)
                .orElse("Nenhum dado de currículo Lattes foi fornecido na base de dados até o momento.");
    }

    @Override
    protected boolean shouldIncludeMilestones() {
        // Injeta a linha do tempo cronológica automaticamente no Prompt Mestre
        return true; 
    }

    @Override
    protected String getEvaluationCriteria() {
        // O direcionamento específico DESTA tarefa agora engloba as novas features
        return "Analise detalhadamente o currículo do aluno (Dados da Entidade) e o seu histórico de conquistas (Marcos). " +
               "1. Se o usuário anexou um Documento de Referência (como um Edital de Mestrado), avalie as chances reais dele ser aprovado com base no currículo e sugira melhorias.\n" +
               "2. Se o usuário fez uma solicitação direta, responda a dúvida focando na área de atuação dele.\n" +
               "3. Caso não haja pergunta específica ou anexo, recomende próximos passos estratégicos e 3 linhas de pesquisa adequadas com base no Lattes e no Histórico.";
    }
}