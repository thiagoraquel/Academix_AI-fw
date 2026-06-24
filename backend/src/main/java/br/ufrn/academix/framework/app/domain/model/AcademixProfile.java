package br.ufrn.academix.framework.app.domain.model;

import br.ufrn.academix.framework.core.auth.FrameworkAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "academix_profiles")
@Getter
@Setter
public class AcademixProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "account_id", unique = true)
    private FrameworkAccount account;

    private String lattesId;
    private String openAlexAuthorId;
    private String academicLevel;

    @Column(name = "curriculo_texto", columnDefinition = "TEXT")
    private String curriculoTexto;
}