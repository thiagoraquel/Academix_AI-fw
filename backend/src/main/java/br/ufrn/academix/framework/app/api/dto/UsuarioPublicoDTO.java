package br.ufrn.academix.framework.app.api.dto;

import lombok.Builder;
import java.util.UUID;

@Builder
public record UsuarioPublicoDTO(
    UUID id,
    String nome,
    String descricaoCurta
) {}