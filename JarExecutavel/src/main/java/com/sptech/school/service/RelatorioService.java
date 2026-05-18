package com.sptech.school.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void gerarRelatorio(String relatorio){
        System.out.println("Gerando relatório...");
    }

    public Path buscarJSON(){
        /*Path caminho = Paths.get(
                "client",
                "empresa_1",
                "c0:35:32:c7:0b:59",
                "dashboard.json"
        );
        Para buscar na S3?
        */
        Path caminho = Paths.get("C:\\Users\\ricar\\Downloads\\dashboard (2).json");
        if(Files.exists(caminho)) {
            System.out.println("Arquivo encontrado.");
            return caminho;
        } else {
            throw new RuntimeException("Arquivo não encontrado.");
        }
    }

    public JsonNode lerJSON(Path caminho) throws IOException {
        ObjectMapper leitor = new ObjectMapper();
        return leitor.readTree(caminho.toFile());
    }

    public String gerarTexto(JsonNode json, List<RelatorioData> mysql) {

        RelatorioData infosUsuario = mysql.getFirst();
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

        for (RelatorioData r : mysql) {

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
        }

        relatorioFinal.append("Fim do relatório.");

        return relatorioFinal.toString().trim();
    }

    public Path salvarPDF(String textoRelatorio) throws IOException {
        //Criando documento
        try(PDDocument document = new PDDocument()) {
            //Criando página
            PDPage page = new PDPage();
            document.addPage(page);

            //Começando escrita
            try(PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                contentStream.beginText();

                //Fonte e tamanho do texto
                contentStream.setFont(new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA),
                        12);

                // posição inicial na página
                contentStream.newLineAtOffset(50, 750);

                //Escrita
                String[] linhas = textoRelatorio.split("\n");

                for(String linha : linhas){
                    contentStream.showText(linha);
                    contentStream.newLineAtOffset(0, -15);
                }

                contentStream.endText();
            }

            //Salvar Relatório
            document.save("relatorio_" + + System.currentTimeMillis()+ "pdf");
            System.out.println("Documento salvo com sucesso.");
            return Paths.get("relatorio.pdf");

        } catch (IOException e) {
            throw new IOException("Erro ao escrever o relatório:" + e);
        }
    }


    /*

    buscar o JSON (OK)
    ler o JSON (OK)
    buscar os dados no BD MySQL (OK)
    juntar dados json e mysql (OK)
    estruturar o texto do relatório (Ok)
    salvar o PDF
    enviar o PDF
    **/
}
