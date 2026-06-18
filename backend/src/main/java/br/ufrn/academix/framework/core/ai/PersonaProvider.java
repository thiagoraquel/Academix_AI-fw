package br.ufrn.academix.framework.core.ai;

public interface PersonaProvider {
    /**
     * Retorna o Prompt Mestre (System Prompt) que define o papel da IA.
     * Ex: "Você é um Conselheiro de Carreira..."
     */
    String getSystemPrompt();

    /**
     * Monta a estrutura final do texto juntando o contexto, documento e a mensagem do usuário.
     */
    String buildContextualizedPrompt(String contextData, String documentText, String userMessage);
}