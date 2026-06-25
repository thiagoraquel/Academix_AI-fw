package br.ufrn.academix.framework.app.api.dto;

import lombok.Builder;

@Builder
public record AreaConhecimentoDTO(
    String area, 
    Long quantidade
) {}