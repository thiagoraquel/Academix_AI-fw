package br.ufrn.academix.framework.app.api.dto;

import lombok.Builder;

@Builder
public record GeneroPorcentagemDTO(
    String genero, 
    Double porcentagem
) {}