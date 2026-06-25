package br.ufrn.academix.framework.app.api.dto;

import lombok.Builder;

@Builder
public record InstituicaoTopDTO(
    String nome, 
    Integer publicacoes
) {}