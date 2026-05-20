package com.sptech.school.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sptech.school.model.Incidente;
import com.sptech.school.service.IncidentService;

import java.io.File;
import java.util.Arrays;
import java.util.List;

    public class App {
        public static void main(String[] args) {

            System.out.println("Iniciando teste local...");

            try {
                // 1. Ler o arquivo JSON local (Simulando o S3)
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                File arquivoJson = new File("incidentes-teste.json");

                List<Incidente> incidentes = Arrays.asList(
                        mapper.readValue(arquivoJson, Incidente[].class)
                );

                System.out.println("Lidos " + incidentes.size() + " incidentes do arquivo local.");

                // 2. Processar os incidentes (Salvar no DB, Criar Jira, Enviar Slack)
                IncidentService incidentService = new IncidentService();
                incidentService.processarIncidentes(incidentes);

                // 3. Verificar incidentes já abertos e atualizar status se resolvidos no Jira
                System.out.println("Verificando se há issues resolvidas no Jira...");
                incidentService.verificarResolvidos();

                System.out.println("Teste local finalizado com sucesso!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*package com.sptech.school;

import com.sptech.school.model.Incidente;
import com.sptech.school.service.IncidentService;
import com.sptech.school.service.S3Service;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Iniciando integração com S3...");

        try {
            // 1. Buscar os incidentes diretamente do S3
            S3Service s3Service = new S3Service();

            // O nome exato do arquivo como está salvo lá no seu bucket
            String chaveDoArquivoNoS3 = "incidentes-teste.json";

            System.out.println("Baixando JSON do S3...");
            List<Incidente> incidentes = s3Service.buscarIncidentes(chaveDoArquivoNoS3);

            System.out.println("Lidos " + incidentes.size() + " incidentes do S3.");

            // 2. Processar (DB, Jira, Slack)
            IncidentService incidentService = new IncidentService();
            incidentService.processarIncidentes(incidentes);

            // 3. Checar os resolvidos
            System.out.println("Verificando atualizações no Jira...");
            incidentService.verificarResolvidos();

            System.out.println("Teste com S3 finalizado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
           /* // Upload de texto
            S3Service.uploadTexto(
                    "Teste de integração Java + AWS S3",
                    "raw/teste.txt"
            );

            // Ler arquivo
            S3Service.lerArquivo(
                    "raw/teste.txt"
            );

            // Verificar existência
            S3Service.arquivoExiste(
                    "raw/teste.txt"
            );

            // Listar arquivos
            S3Service.listarArquivos(
                    "raw/"
            );

            // Download
            S3Service.baixarArquivo(
                    "raw/teste.txt",
                    "download_teste.txt"
            );

            // Upload de arquivo local
            S3Service.uploadArquivo(
                    "download_teste.txt",
                    "backup/download_teste.txt"
            );
        }
    }*/
        /* RELATÓRIO

        //Conexão com MySQL
        MySQLConnection conexao = new MySQLConnection();

        //Buscando dados no MySQL
        String usuario =  "ricardo@horus.com";
        String servidor = "Nathan";
        RelatorioRepository data = new RelatorioRepository();
        List<RelatorioData> dadosBanco = data.buscarDados(usuario, servidor);

        //Buscando dados no JSON
        RelatorioService relatorioService = new RelatorioService();
        Path caminho = relatorioService.buscarJSON();
        JsonNode json = relatorioService.lerJSON(caminho);

        String relatorio = relatorioService.gerarTexto(json, dadosBanco);

        System.out.println(relatorioService.salvarPDF(relatorio, usuario)); */

        /* CONEXÃO JIRA - SLACK

        JSONObject json = new JSONObject();

        String baseUrl = "https://horusmonitoring.atlassian.net";
        String email = "horusmonitoring@outlook.com.br";
        String apiToken = "";
        Jira jira = new Jira(baseUrl, email, apiToken);

        while (true){
            JarFinal log = new JarFinal();
            String mensagem = log.logHardware();
            json.put("text", mensagem);
            System.out.println(mensagem);

            if(mensagem != null){
                String response = jira.createIssue(
                        "KAN",
                        log.getEvento(),
                        "Task",
                        log.getNivelChamado()
                );
            }

            Slack.sendMessage(json);
            Thread.sleep(10000);
        }*/



