package com.sptech.school.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.sptech.school.JarFinal;
import com.sptech.school.config.Jira;
import com.sptech.school.config.MySQLConnection;
import com.sptech.school.config.Slack;
import com.sptech.school.JarFinal;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.sptech.school.service.RelatorioData;
import com.sptech.school.service.RelatorioRepository;
import com.sptech.school.service.RelatorioService;
import org.json.JSONObject;


public class App {

    public static void main(String[] args) throws Exception {

        //Conexão com MySQL
        MySQLConnection conexao = new MySQLConnection();

        //Buscando dados no MySQL
        RelatorioRepository data = new RelatorioRepository();
        List<RelatorioData> dadosBanco = data.buscarDados("mariana@horus.com", "Nathan");

        //Buscando dados no JSON
        RelatorioService relatorioService = new RelatorioService();
        Path caminho = relatorioService.buscarJSON();
        JsonNode json = relatorioService.lerJSON(caminho);

        String relatorio = relatorioService.gerarTexto(json, dadosBanco);

        System.out.println(relatorioService.salvarPDF(relatorio));

        /*JSONObject json = new JSONObject();

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
}
