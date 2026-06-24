package br.ufrn.academix.framework.app.api.dto;

public record RegistroDTO(
    String nome,
    String email,
    String senha,
    String lattesId,
    String academicLevel
) {}