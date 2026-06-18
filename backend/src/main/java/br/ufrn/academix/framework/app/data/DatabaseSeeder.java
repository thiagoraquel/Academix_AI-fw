package br.ufrn.academix.framework.app.data;

import br.ufrn.academix.framework.app.domain.AcademixUser;
import br.ufrn.academix.framework.app.domain.AcademixUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final AcademixUserRepository repository;

    public DatabaseSeeder(AcademixUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        // Só insere se a tabela estiver vazia
        if (repository.count() == 0) {
            List<AcademixUser> mockUsers = new ArrayList<>();

            for (int i = 1; i <= 20; i++) {
                AcademixUser user = new AcademixUser();
                user.setEmail("estudante" + i + "@ufrn.edu.br");
                user.setName("Estudante Mock " + i);
                user.setPassword("senha_temporaria"); // No futuro aplicaremos BCrypt aqui
                user.setLattesId("1234567890123" + String.format("%03d", i));
                user.setOpenAlexAuthorId("A" + i * 1000);
                user.setAcademicLevel(i % 2 == 0 ? "Graduação" : "Mestrado");
                
                mockUsers.add(user);
            }

            repository.saveAll(mockUsers);
            System.out.println("20 usuários mockados inseridos com sucesso no PostgreSQL!");
        }
    }
}