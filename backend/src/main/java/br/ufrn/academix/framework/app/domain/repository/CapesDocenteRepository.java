package br.ufrn.academix.framework.app.domain.repository;

import br.ufrn.academix.framework.app.api.dto.ItemContagemDTO;
import br.ufrn.academix.framework.app.domain.model.CapesDocente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapesDocenteRepository extends JpaRepository<CapesDocente, Long> {

    @Query("SELECT c.nmAreaConhecimento, COUNT(c) FROM CapesDocente c WHERE c.nmAreaConhecimento IS NOT NULL GROUP BY c.nmAreaConhecimento ORDER BY COUNT(c) DESC")
    List<Object[]> contarDocentesPorArea();

    @Query("SELECT COUNT(c) FROM CapesDocente c WHERE c.nmEntidadeEnsino ILIKE %:nome%")
    Long contarTotalDocentesDaUniversidade(@Param("nome") String nome);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(c.dsFaixaEtaria, COUNT(c)) " +
           "FROM CapesDocente c " +
           "WHERE c.nmEntidadeEnsino ILIKE %:nome% AND c.dsFaixaEtaria IS NOT NULL " +
           "GROUP BY c.dsFaixaEtaria " +
           "ORDER BY COUNT(c) DESC")
    List<ItemContagemDTO> contarDistribuicaoFaixaEtariaDaUniversidade(@Param("nome") String nome);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(c.nmAreaAvaliacao, COUNT(c)) " +
           "FROM CapesDocente c " +
           "WHERE c.nmEntidadeEnsino ILIKE %:nome% AND c.nmAreaAvaliacao IS NOT NULL " +
           "GROUP BY c.nmAreaAvaliacao " +
           "ORDER BY COUNT(c) DESC")
    List<ItemContagemDTO> contarTopAreasAvaliacaoDaUniversidade(@Param("nome") String nome);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(d.nmGrandeAreaConhecimento, COUNT(d)) " +
           "FROM CapesDocente d WHERE d.nmEntidadeEnsino = :universidade " +
           "AND d.nmGrandeAreaConhecimento IS NOT NULL AND TRIM(d.nmGrandeAreaConhecimento) <> '' " +
           "AND UPPER(d.nmGrandeAreaConhecimento) NOT LIKE '%NÃO INFORMADO%' AND UPPER(d.nmGrandeAreaConhecimento) NOT LIKE '%NAO INFORMADO%' " +
           "GROUP BY d.nmGrandeAreaConhecimento ORDER BY COUNT(d) DESC")
    List<ItemContagemDTO> contarTopGrandesAreasDaUniversidade(@Param("universidade") String universidade);

    @Query("SELECT new br.ufrn.academix.framework.app.api.dto.ItemContagemDTO(d.cdConceitoPrograma, COUNT(d)) " +
           "FROM CapesDocente d WHERE d.nmEntidadeEnsino = :universidade " +
           "AND d.cdConceitoPrograma IS NOT NULL AND TRIM(d.cdConceitoPrograma) <> '' " +
           "GROUP BY d.cdConceitoPrograma ORDER BY d.cdConceitoPrograma ASC")
    List<ItemContagemDTO> contarDistribuicaoConceitoProgramaDaUniversidade(@Param("universidade") String universidade);
}