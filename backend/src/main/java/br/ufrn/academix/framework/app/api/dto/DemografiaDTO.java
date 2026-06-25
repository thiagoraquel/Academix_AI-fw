package br.ufrn.academix.framework.app.api.dto;

import lombok.Builder;

@Builder
public record DemografiaDTO(
    String categoria, 
    Long totalAbsoluto, 
    Double porcentagem
) {}