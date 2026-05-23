package com.sptech.school.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sptech.school.model.Relatorio;
import com.sptech.school.repository.RelatorioRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class RelatorioService {

    private final S3Service s3Service = new S3Service();

    /**
     * Ponto de entrada chamado pelo App no modo relatório.
     * Retorna o Path do PDF gerado para que o Node.js possa fazer o download.
     */
    public Path BotaoRelatorio(String usuario, String email,
                               String hostname, String mac_address,
                               Integer id) throws Exception {

        // Monta o caminho do dashboard no S3
        String chaveS3 = String.format(
                "client/empresa_%d/%s/dashboard_rede_24h.json", id, mac_address
        );

        return processarRelatorio(email, hostname, chaveS3);
    }

    public JsonNode buscarDashboardDoS3(String chaveS3) throws IOException {
        String jsonContent = s3Service.obterConteudoComoString(chaveS3);
        ObjectMapper leitor = new ObjectMapper();
        return leitor.readTree(jsonContent);
    }

    public Path processarRelatorio(String usuario, String host,
                                   String chaveS3) throws Exception {
        // Busca dados no banco
        RelatorioRepository repo = new RelatorioRepository();
        List<Relatorio> mysqlDados = repo.buscarDados(usuario, host);

        // Busca o JSON no S3
        JsonNode json = buscarDashboardDoS3(chaveS3);

        // Gera o texto e salva PDF
        String texto = gerarTexto(json, mysqlDados);

        return salvarPDF(texto, usuario);
    }

    public String gerarTexto(JsonNode json, List<Relatorio> mysql) {
        if (mysql == null || mysql.isEmpty()) {
            System.err.println("Nenhum dado encontrado no banco para este usuário/servidor.");
            return "Nenhum dado encontrado para gerar o relatório.";
        }

        Relatorio infosUsuario = mysql.get(0);
        StringBuilder relatorioFinal = new StringBuilder();

        relatorioFinal.append(String.format(
                """
                Relatório de Mudança de Turno
                
                Usuário solicitante: %s
                CPF: %s
                Email: %s
                Função: %s da %s
                
                ----- Informações do Servidor -----
                Mac Address: %s
                Nome: %s
                Status do Servidor: %s
                
                """,
                infosUsuario.getNome(),
                infosUsuario.getCpf(),
                infosUsuario.getEmail(),
                infosUsuario.getFuncao(),
                infosUsuario.getRazaoSocial(),
                infosUsuario.getMacAddress(),
                infosUsuario.getHostname(),
                infosUsuario.getStatusServidor()
        ));

        relatorioFinal.append(String.format(
                """
                ----- Métricas das Últimas 24h -----
                
                Perda de pacotes: %s%%
                Latência média: %s ms
                Taxa ADS-B: %s%%
                Rotas impactadas: %s
                
                Perda por serviço:
                Rastreamento: %s%%
                Rotas: %s%%
                Correlação: %s%%
                API Gateway: %s%%
                Banco de Dados: %s%%
                Sync Service: %s%%
                
                ----- Logs de Alertas -----
                
                """,
                json.get("kpis").get("perda_pacotes"),
                json.get("kpis").get("latencia_media"),
                json.get("kpis").get("adsb_update"),
                json.get("kpis").get("rotas_sem_atualizacao"),
                json.get("perda_pacotes_servico").get("Rastreamento"),
                json.get("perda_pacotes_servico").get("Rotas"),
                json.get("perda_pacotes_servico").get("Correlação"),
                json.get("perda_pacotes_servico").get("API Gateway"),
                json.get("perda_pacotes_servico").get("Banco de Dados"),
                json.get("perda_pacotes_servico").get("Sync Service")
        ));

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Relatorio r : mysql) {
            if (r.getDataAlerta() != null) {
                relatorioFinal.append(String.format(
                        """
                                ----------------------------
                                Data: %s
                                Criticidade: %s
                                Status servidor: %s
                                Componente afetado: %s
                                Limite definido: %s %s
                                Situação alerta: %s
                                
                                """,
                        r.getDataAlerta().format(formatter),
                        r.getCriticidade(),
                        r.getStatusServidor(),
                        r.getTipoComponente(),
                        r.getLimite(),
                        r.getUnidadeMedida(),
                        r.getStatusAlerta()
                ));
            } else {
                relatorioFinal.append("----------------------------\n")
                        .append("Sem alertas registrados para este componente.\n\n");
            }
        }

        relatorioFinal.append("Fim do relatório.");
        return relatorioFinal.toString().trim();
    }

    public Path salvarPDF(String textoRelatorio, String usuario) throws IOException {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream =
                         new PDPageContentStream(document, page)) {

                contentStream.beginText();
                contentStream.setFont(
                        new PDType1Font(Standard14Fonts.FontName.HELVETICA),
                        12
                );
                contentStream.newLineAtOffset(50, 750);

                String[] linhas = textoRelatorio.split("\n");
                for (String linha : linhas) {
                    contentStream.showText(linha);
                    contentStream.newLineAtOffset(0, -15);
                }

                contentStream.endText();
            }

            Path pastaRelatorios = Paths.get("relatorios");
            if (!Files.exists(pastaRelatorios)) {
                Files.createDirectories(pastaRelatorios);
            }
            usuario = usuario.replaceAll("[^a-zA-Z0-9_-]", "_");

            String nomeArquivo = usuario + "_" + System.currentTimeMillis() + ".pdf";
            Path caminhoArquivo = pastaRelatorios.resolve(nomeArquivo);

            document.save(caminhoArquivo.toFile());
            return caminhoArquivo;

        } catch (IOException e) {
            throw new IOException("Erro ao escrever o relatório: " + e.getMessage());
        }
    }
}