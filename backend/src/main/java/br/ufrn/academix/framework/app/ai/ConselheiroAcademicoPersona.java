package br.ufrn.academix.framework.app.ai;

import br.ufrn.academix.framework.core.ai.PersonaProvider;
import org.springframework.stereotype.Component;

@Component
public class ConselheiroAcademicoPersona implements PersonaProvider {

    @Override
    public String getSystemPrompt() {
        // O Prompt Mestre da Instância 1 (Academix AI)
        return "Você é o Conselheiro Acadêmico Inteligente do sistema Academix AI. " +
               "Seu objetivo é avaliar as chances do aluno ser aprovado em editais e sugerir " +
               "melhorias na trajetória acadêmica de forma direta, encorajadora e baseada estritamente no currículo fornecido.";
    }

    @Override
    public String buildContextualizedPrompt(String contextData, String documentText, String userMessage) {
        StringBuilder promptBuilder = new StringBuilder();
        
        // 1. Injeta a Persona
        promptBuilder.append(getSystemPrompt()).append("\n\n");
        
        // 2. Injeta os Dados do Lattes/OpenAlex (Origem dos Dados)
        promptBuilder.append("=== CURRÍCULO DO ALUNO (Base de Dados) ===\n");
        promptBuilder.append(contextData).append("\n\n");

        // 3. Injeta o Edital (Natureza do Documento), se houver
        if (documentText != null && !documentText.trim().isEmpty()) {
            promptBuilder.append("=== DOCUMENTO DE REFERÊNCIA (Ex: Edital de Vaga) ===\n");
            promptBuilder.append(documentText).append("\n\n");
        }

        // 4. Injeta a Pergunta do Usuário
        promptBuilder.append("=== SOLICITAÇÃO DO ALUNO ===\n");
        promptBuilder.append(userMessage);

        return promptBuilder.toString();
    }
}