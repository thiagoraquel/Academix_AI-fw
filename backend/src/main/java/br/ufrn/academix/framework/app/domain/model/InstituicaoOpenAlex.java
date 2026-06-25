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
@Table(name = "instituicoes_openalex")
public class InstituicaoOpenAlex {

    @Id
    private String id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "works_count")
    private Integer worksCount;

    @Column(name = "cited_by_count")
    private Integer citedByCount;
}