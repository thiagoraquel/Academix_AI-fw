package br.ufrn.academix.framework.app.service;

import br.ufrn.academix.framework.app.api.dto.ComparacaoUniversidadeDTO;
import br.ufrn.academix.framework.app.api.dto.DemografiaDTO;
import br.ufrn.academix.framework.app.api.dto.ItemContagemDTO;
import br.ufrn.academix.framework.app.domain.model.InstituicaoOpenAlex;
import br.ufrn.academix.framework.app.domain.repository.CapesDocenteRepository;
import br.ufrn.academix.framework.app.domain.repository.InstituicaoOpenAlexRepository;
import br.ufrn.academix.framework.app.domain.repository.LattesPainelRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Service
public class ComparacaoUniversidadeService {

    private final InstituicaoOpenAlexRepository openAlexRepo;
    private final CapesDocenteRepository capesRepo;
    private final LattesPainelRepository lattesRepo;

    public ComparacaoUniversidadeService(
            InstituicaoOpenAlexRepository openAlexRepo,
            CapesDocenteRepository capesRepo,
            LattesPainelRepository lattesRepo) {
        this.openAlexRepo = openAlexRepo;
        this.capesRepo = capesRepo;
        this.lattesRepo = lattesRepo;
    }

    public ComparacaoUniversidadeDTO obterDadosComparacao(String nomeUniversidade) {
        
        // --- OPENALEX ---
        CompletableFuture<List<InstituicaoOpenAlex>> openAlexOptFuture = CompletableFuture.supplyAsync(() -> 
            openAlexRepo.buscarPorNome(nomeUniversidade)
        );

        // --- CAPES ---
        String nomeCapesProcessado = nomeUniversidade.toUpperCase();

        CompletableFuture<Long> totalDocentesFuture = CompletableFuture.supplyAsync(() -> 
            capesRepo.contarTotalDocentesDaUniversidade(nomeCapesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> faixaEtariaFuture = CompletableFuture.supplyAsync(() -> 
            capesRepo.contarDistribuicaoFaixaEtariaDaUniversidade(nomeCapesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> topAreasFuture = CompletableFuture.supplyAsync(() -> 
            capesRepo.contarTopAreasAvaliacaoDaUniversidade(nomeCapesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> grandesAreasFuture = CompletableFuture.supplyAsync(() -> 
            capesRepo.contarTopGrandesAreasDaUniversidade(nomeCapesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> conceitoFuture = CompletableFuture.supplyAsync(() -> 
            capesRepo.contarDistribuicaoConceitoProgramaDaUniversidade(nomeCapesProcessado)
        );

        // --- LATTES ---
        String nomeLattesProcessado = normalizarParaLattes(nomeUniversidade);

        CompletableFuture<Long> totalFormadosFuture = CompletableFuture.supplyAsync(() -> 
            lattesRepo.contarTotalFormadosDaUniversidade(nomeLattesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> topPaisesFuture = CompletableFuture.supplyAsync(() -> 
            lattesRepo.contarTopPaisesNascimentoDaUniversidade(nomeLattesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> instituicoesAtuacaoFuture = CompletableFuture.supplyAsync(() -> 
            lattesRepo.contarTopInstituicoesAtuacaoDaUniversidade(nomeLattesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> paisesAtuacaoFuture = CompletableFuture.supplyAsync(() -> 
            lattesRepo.contarTopPaisesAtuacaoDaUniversidade(nomeLattesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> sexoBrutoFuture = CompletableFuture.supplyAsync(() -> 
            lattesRepo.contarDistribuicaoSexoDaUniversidade(nomeLattesProcessado)
        );
        CompletableFuture<List<ItemContagemDTO>> racaBrutaFuture = CompletableFuture.supplyAsync(() -> 
            lattesRepo.contarDistribuicaoRacaDaUniversidade(nomeLattesProcessado)
        );
        
        List<InstituicaoOpenAlex> openAlexOpt = openAlexOptFuture.join();
        InstituicaoOpenAlex instituicao = openAlexOpt.isEmpty() ? null : openAlexOpt.get(0);
        Integer totalArtigos = (instituicao != null) ? instituicao.getWorksCount() : 0;
        Integer totalCitacoes = (instituicao != null) ? instituicao.getCitedByCount() : 0;

        Long totalDocentes = totalDocentesFuture.join();
        if (totalDocentes == null) totalDocentes = 0L;

        Long totalFormados = totalFormadosFuture.join();
        if (totalFormados == null) totalFormados = 0L;

        List<DemografiaDTO> distribuicaoSexo = calcularPorcentagemDemografia(sexoBrutoFuture.join());
        List<DemografiaDTO> distribuicaoRaca = calcularPorcentagemDemografia(racaBrutaFuture.join());
        
        return ComparacaoUniversidadeDTO.builder()
            .nomeUniversidade(nomeUniversidade)
            .totalArtigos(totalArtigos)
            .totalCitacoes(totalCitacoes)
            .totalDocentes(totalDocentes)
            .distribuicaoFaixaEtaria(faixaEtariaFuture.join())
            .topAreasAvaliacao(topAreasFuture.join())
            .topGrandesAreasConhecimento(grandesAreasFuture.join())
            .distribuicaoConceitoPrograma(conceitoFuture.join())
            .totalFormados(totalFormados)
            .topPaisesNascimento(topPaisesFuture.join())
            .topInstituicoesAtuacao(instituicoesAtuacaoFuture.join())
            .topPaisesAtuacao(paisesAtuacaoFuture.join())
            .distribuicaoSexo(distribuicaoSexo)
            .distribuicaoRaca(distribuicaoRaca)
            .build();
    }

    private List<DemografiaDTO> calcularPorcentagemDemografia(List<ItemContagemDTO> dadosBrutos) {
        if (dadosBrutos == null || dadosBrutos.isEmpty()) {
            return List.of();
        }

        // Ajustado para .contagem() ao invés de .quantidade() para bater com o novo Record
        long totalGeral = dadosBrutos.stream()
                .mapToLong(ItemContagemDTO::contagem)
                .sum();

        if (totalGeral == 0) return List.of();

        return dadosBrutos.stream().map(item -> {
            double porcentagemExata = (item.contagem() * 100.0) / totalGeral;
            double porcentagemArredondada = Math.round(porcentagemExata * 100.0) / 100.0;
            
            // Ajustado para .rotulo() ao invés de .chave()
            return DemografiaDTO.builder()
                    .categoria(item.rotulo())
                    .totalAbsoluto(item.contagem())
                    .porcentagem(porcentagemArredondada)
                    .build();
        }).toList();
    }

    public List<String> buscarSugestoes(String termo) {
        if (termo == null || termo.trim().length() < 3) {
            return List.of();
        }
        return openAlexRepo.buscarSugestoesLimpas(termo);
    }

    private String normalizarParaLattes(String nome) {
        if (nome == null) return null;
        
        String normalizado = Normalizer.normalize(nome, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String semAcento = pattern.matcher(normalizado).replaceAll("");
        
        return "%" + semAcento + "%";
    }
}