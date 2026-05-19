package com.sptech.school.app;

import com.sptech.school.config.S3Provider;
import com.sptech.school.service.S3Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;


public class App {

        public static void main(String[] args) {

            // Upload de texto
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
    }
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



