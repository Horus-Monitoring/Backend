package com.sptech.school.service;

import com.sptech.school.config.Jira;
import com.sptech.school.config.Slack;
import com.sptech.school.model.Incidente;
import com.sptech.school.repository.IncidenteRepository;
import org.json.JSONObject;

import java.util.List;

public class IncidentService {

    private Jira jira = new Jira();

    private IncidenteRepository repository = new IncidenteRepository();

    public void processarIncidentes(List<Incidente> incidentes) {

        for(Incidente incidente : incidentes){

            try {
                boolean existe = repository.existe(incidente.getChave());

                if(existe){
                    System.out.println("Incidente já registrado.");
                    continue;
                }

                String prioridade = mapearPrioridade(incidente.getCriticidade());

                String respostaJira = jira.createIssue(
                        "KAN",
                        incidente.getTitulo(),
                        "Task",
                        prioridade
                );

                String jiraKey = extrairKeyJira(respostaJira);

                incidente.setChave(jiraKey);
                incidente.setStatusAlerta("Ativo");

                repository.salvar(incidente);

                JSONObject slackMensagem = new JSONObject();

                slackMensagem.put(
                        "text",
                        """
                        🚨 Novo incidente detectado
                        
                        Título: %s
                        Criticidade: %s
                        ID Servidor: %s
                        Jira: %s
                        """.formatted(
                                incidente.getTitulo(),
                                incidente.getCriticidade(),
                                incidente.getFkServidor(),
                                jiraKey
                        )
                );

                Slack.sendMessage(slackMensagem);

                System.out.println("Incidente processado.");

            } catch (Exception e) {

                System.out.println(
                        "Erro ao processar incidente: " + e.getMessage()
                );
            }
        }
    }

    public void verificarResolvidos() {

        List<Incidente> incidentes = repository.buscarAbertos();

        for(Incidente incidente : incidentes){

            try {
                String status = jira.buscarStatusIssue(incidente.getChave());

                if(
                        status.equalsIgnoreCase("Done")
                                || status.equalsIgnoreCase("Resolved")
                                || status.equalsIgnoreCase("Closed")
                ){
                    repository.marcarResolvido(incidente.getChave());

                    System.out.println(
                            "Incidente resolvido no Jira: " + incidente.getChave()
                                    + " | Chave DB: " + incidente.getChave()
                    );
                }

            } catch (Exception e){

                System.out.println(
                        "Erro ao verificar incidente: " + e.getMessage()
                );
            }
        }
    }

    private String mapearPrioridade(String criticidade){

        return switch (criticidade.toLowerCase()) {
            case "crítico" -> "Highest";
            case "alto" -> "High";
            case "medio", "médio" -> "Medium";
            case "baixo" -> "Low";
            default -> "Lowest";
        };
    }

    private String extrairKeyJira(String resposta){

        try {
            JSONObject json = new JSONObject(resposta);
            return json.getString("key");
        } catch (Exception e){
            throw new RuntimeException("Erro ao extrair chave Jira.");
        }
    }
}