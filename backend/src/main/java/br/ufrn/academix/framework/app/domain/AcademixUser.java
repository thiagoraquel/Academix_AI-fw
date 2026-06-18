package br.ufrn.academix.framework.app.domain;

import br.ufrn.academix.framework.core.auth.BaseAccount;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "academix_users")
@Getter
@Setter
public class AcademixUser extends BaseAccount {

    private String lattesId;
    private String openAlexAuthorId;
    private String academicLevel; // ex: Graduação, Mestrado

    @Override
    public String getBusinessIdentifier() {
        return this.lattesId; // Para a Instância 1, o Lattes é o ID principal
    }
}