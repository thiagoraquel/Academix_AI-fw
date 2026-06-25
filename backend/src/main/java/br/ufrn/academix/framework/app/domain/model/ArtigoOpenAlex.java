package br.ufrn.academix.framework.app.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "openalex_artigos")
public class ArtigoOpenAlex {

    @Id
    @Column(name = "id_openalex")
    private String idOpenAlex;

    @Column(name = "tipo")
    private String tipo;

    // Outras colunas omitidas propositalmente para economizar processamento e memória,
    // já que o escopo atual não necessita delas.
}