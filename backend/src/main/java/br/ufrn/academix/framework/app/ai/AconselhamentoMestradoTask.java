package br.ufrn.academix.framework.app.ai;

import br.ufrn.academix.framework.core.ai.AiTaskTemplate;
import br.ufrn.academix.framework.core.ai.LlmClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AconselhamentoMestradoTask extends AiTaskTemplate {

    // Aqui no futuro você injetará o repository ou provider para buscar o currículo no BD
    // private final AcademixIdentifierProvider identifierProvider;

    public AconselhamentoMestradoTask(LlmClient llmClient) {
        super(llmClient);
    }

    @Override
    protected String getSystemPersona() {
        return "Você é o Conselheiro Acadêmico Inteligente do sistema Academix AI. " +
               "Seu tom deve ser direto, profissional e focado em impulsionar a trajetória acadêmica e de pesquisa do estudante.";
    }

    @Override
    protected String extractProfileData(UUID accountId) {
        // Exemplo: O framework pede os dados, a aplicação vai no banco e formata a String.
        // String lattesId = identifierProvider.getIdentifierForAccount(accountId);
        // return "Buscando dados XML do Lattes: " + lattesId; 
        
        return "Aluno de Ciência da Computação na UFRN. Foco em Engenharia de Software e IA.";
    }

    @Override
    protected String getEvaluationCriteria() {
        // O direcionamento específico DESTA tarefa
        return "Analise detalhadamente o currículo do aluno fornecido acima. " +
               "Com base nas disciplinas e interesses demonstrados, recomende 3 linhas de pesquisa " +
               "ou programas de mestrado adequados. Ignore opções não relacionadas à computação.";
    }
}