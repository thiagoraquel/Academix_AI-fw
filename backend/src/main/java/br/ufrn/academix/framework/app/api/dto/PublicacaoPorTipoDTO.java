package br.ufrn.academix.framework.app.api.dto;

import lombok.Builder;

@Builder
public record PublicacaoPorTipoDTO(
    String tipo, 
    Long quantidade
) {}