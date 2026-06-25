package br.ufrn.academix.framework.app.domain.repository;

import br.ufrn.academix.framework.app.api.dto.ItemContagemDTO;
import br.ufrn.academix.framework.app.domain.model.LattesPainel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LattesPainelRepository extends JpaRepository<LattesPainel, Long> {
    
    @Query("SELECT l.sexo, SUM(l.contagemRegistro) FROM LattesPainel l WHERE l.sexo IS NOT NULL GROUP BY l.sexo")
    List<Object[]> contarRegistrosPorSexo();

    @Query("SELECT SUM(l.contagemRegistro) FROM LattesPainel l WHERE l.instituicaoFormacao ILIKE :nome")
    Long contarTotalFormadosDaUniversidade(@Param("nome") String nome);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(l.paisNascimento, SUM(l.contagemRegistro)) " +
           "FROM LattesPainel l " +
           "WHERE l.instituicaoFormacao ILIKE :nome " +
           "AND l.paisNascimento IS NOT NULL AND l.paisNascimento <> 'Não Informado' " +
           "GROUP BY l.paisNascimento " +
           "ORDER BY SUM(l.contagemRegistro) DESC")
    List<ItemContagemDTO> contarTopPaisesNascimentoDaUniversidade(@Param("nome") String nome);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(l.sexo, SUM(l.contagemRegistro)) " +
           "FROM LattesPainel l " +
           "WHERE l.instituicaoFormacao ILIKE :nome AND l.sexo IS NOT NULL " +
           "GROUP BY l.sexo")
    List<ItemContagemDTO> contarDistribuicaoSexoDaUniversidade(@Param("nome") String nome);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(l.corRaca, SUM(l.contagemRegistro)) " +
           "FROM LattesPainel l " +
           "WHERE l.instituicaoFormacao ILIKE :nome AND l.corRaca IS NOT NULL AND l.corRaca <> 'Não Informado' " +
           "GROUP BY l.corRaca " +
           "ORDER BY SUM(l.contagemRegistro) DESC")
    List<ItemContagemDTO> contarDistribuicaoRacaDaUniversidade(@Param("nome") String nome);
    
    @Query("SELECT SUM(l.contagemRegistro) FROM LattesPainel l " +
           "WHERE l.instituicaoFormacao ILIKE :nome AND l.instituicaoAtuacao ILIKE :nome")
    Long contarRetidosNaMesmaInstituicao(@Param("nome") String nome);

    @Query(value = "SELECT DISTINCT instituicao_formacao FROM lattes_painel " +
                   "WHERE instituicao_formacao ILIKE CONCAT('%', :termo, '%') " +
                   "AND instituicao_formacao IS NOT NULL " +
                   "LIMIT 10", nativeQuery = true)
    List<String> buscarSugestoesDeNomes(@Param("termo") String termo);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(l.instituicaoAtuacao, COUNT(l)) " +
           "FROM LattesPainel l WHERE l.instituicaoFormacao LIKE :universidade " +
           "AND l.instituicaoAtuacao IS NOT NULL " +
           "AND UPPER(l.instituicaoAtuacao) NOT LIKE '%NÃO INFORMADO%' " +
           "AND UPPER(l.instituicaoAtuacao) NOT LIKE '%NAO INFORMADO%' " +
           "GROUP BY l.instituicaoAtuacao ORDER BY COUNT(l) DESC")
    List<ItemContagemDTO> contarTopInstituicoesAtuacaoDaUniversidade(@Param("universidade") String universidade);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(l.paisAtuacao, COUNT(l)) " +
           "FROM LattesPainel l WHERE l.instituicaoFormacao LIKE :universidade AND l.paisAtuacao IS NOT NULL " +
           "AND UPPER(l.paisAtuacao) NOT LIKE '%NÃO INFORMADO%' AND UPPER(l.paisAtuacao) NOT LIKE '%NAO INFORMADO%' " +
           "GROUP BY l.paisAtuacao ORDER BY COUNT(l) DESC")
    List<ItemContagemDTO> contarTopPaisesAtuacaoDaUniversidade(@Param("universidade") String universidade);
}