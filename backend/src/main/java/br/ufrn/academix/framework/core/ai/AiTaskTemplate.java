package br.ufrn.academix.framework.core.ai;

import java.util.UUID;

public abstract class AiTaskTemplate {

    private final LlmClient llmClient;

    public AiTaskTemplate(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    /**
     * O "Template Method": O esqueleto imutável do framework.
     * Coordena a montagem do prompt e a chamada da IA.
     */
    public final String executeTask(UUID accountId, String documentText, String userMessage) {
        StringBuilder promptBuilder = new StringBuilder();

        // 1. Injeta a Persona (Ponto Variável, mas Obrigatório)
        promptBuilder.append(getSystemPersona()).append("\n\n");

        // 2. Obtém e injeta os Dados do Usuário (Ponto Variável, Opcional dependendo da tarefa)
        String profileData = extractProfileData(accountId);
        if (profileData != null && !profileData.isBlank()) {
            promptBuilder.append("=== DADOS DA ENTIDADE (BASE DE DADOS) ===\n");
            promptBuilder.append(profileData).append("\n\n");
        }

        // 3. Injeta o Documento de Referência (Opcional fornecido pelo usuário)
        if (documentText != null && !documentText.isBlank()) {
            promptBuilder.append("=== DOCUMENTO DE REFERÊNCIA ===\n");
            promptBuilder.append(documentText).append("\n\n");
        }

        // 4. Injeta a Solicitação/Pergunta do Usuário
        if (userMessage != null && !userMessage.isBlank()) {
            promptBuilder.append("=== SOLICITAÇÃO DO USUÁRIO ===\n");
            promptBuilder.append(userMessage).append("\n\n");
        }

        // 5. Injeta o Critério de Avaliação/Direcionamento (Ponto Variável)
        promptBuilder.append("=== CRITÉRIO DE AVALIAÇÃO / DIRECIONAMENTO ===\n");
        promptBuilder.append(getEvaluationCriteria());

        // 6. Dispara para a LLM (Gemini, OpenAI, etc.)
        return llmClient.generateResponse(promptBuilder.toString());
    }

    // --- PONTOS VARIÁVEIS (Hooks) ---
    // Os desenvolvedores da aplicação são obrigados a implementar estes métodos.

    protected abstract String getSystemPersona();

    protected abstract String extractProfileData(UUID accountId);

    protected abstract String getEvaluationCriteria();
}