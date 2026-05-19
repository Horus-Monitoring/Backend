package com.sptech.school.app;

import com.sptech.school.config.S3Provider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;


public class App {

    public static void main(String[] args) throws Exception {

        public static void main(String[] args) {

            S3Service s3 = new S3Service();

            s3.uploadArquivo(
                    "teste.txt",
                    "raw/teste.txt"
            );
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

    }

