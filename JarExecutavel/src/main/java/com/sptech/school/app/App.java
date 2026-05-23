package com.sptech.school.app;

import com.sptech.school.model.Incidente;
import com.sptech.school.service.IncidentService;
import com.sptech.school.service.RelatorioService;
import com.sptech.school.service.S3Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import com.sptech.school.provider.S3Provider;
import com.sptech.school.config.S3Connection;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) throws Exception {
        // -------------------------------------------------------
        // MODO RELATÓRIO
        // Chamado pelo Node.js via: java -jar relatorio.jar
        //     <usuario> <email> <mac_address> <servidor> <id_empresa>
        // -------------------------------------------------------
        if (args.length == 5) {
            modoRelatorio(args);
            return;
        }

        // -------------------------------------------------------
        // MODO DAEMON — loop de escaneamento do S3 a cada 1 hora
        // -------------------------------------------------------
        System.out.println("Iniciando daemon de monitoramento S3...");
        System.out.println(S3Connection.getACCESS_KEY());
        modoDaemon();
    }

    // ------------------------------------------------------------------
    // MODO RELATÓRIO
    // ------------------------------------------------------------------
    private static void modoRelatorio(String[] args) {
        String usuario    = args[0];
        String email      = args[1];
        String macAddress = args[2].toLowerCase();
        String servidor   = args[3];
        int    idEmpresa  = Integer.parseInt(args[4]);

        System.err.println("Gerando relatório para: " + email);

        try {
            RelatorioService service = new RelatorioService();
            Path caminhoPDF = service.BotaoRelatorio(usuario, email, servidor, macAddress, idEmpresa);

            // Imprime o caminho absoluto no stdout — o Node.js lê esse valor
            System.out.println(caminhoPDF.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // ------------------------------------------------------------------
    // MODO DAEMON
    // ------------------------------------------------------------------
    private static void modoDaemon() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Executa imediatamente e depois a cada 1 hora
        scheduler.scheduleAtFixedRate(
                App::escanearS3,
                0,
                1,
                TimeUnit.HOURS
        );

        // Mantém a JVM viva
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Encerrando daemon...");
            scheduler.shutdownNow();
        }));
    }

    // ------------------------------------------------------------------
    // ESCANEAMENTO DO S3
    // Varre o prefixo "client/" procurando arquivos de incidentes
    // Estrutura esperada: client/empresa_<id>/<mac_address>/incidentes.json
    // ------------------------------------------------------------------
    private static void escanearS3() {
        System.out.println("Iniciando varredura S3 em: " + java.time.LocalDateTime.now());

        try {
            S3Client s3         = S3Provider.criarCliente();
            String   bucket     = S3Connection.getBUCKET_NAME();
            String   prefixo    = "client/alertas/";

            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .prefix(prefixo)
                    .build();

            ListObjectsV2Response listResponse = s3.listObjectsV2(listRequest);

            S3Service       s3Service       = new S3Service();
            IncidentService incidentService = new IncidentService();

            for (S3Object objeto : listResponse.contents()) {

                System.out.println("Listando objetos");
                System.out.println(objeto);

                String chave = objeto.key();

                // Processa apenas arquivos de incidentes
                if (!chave.endsWith("incidentes_rede_24h.json")) { // inserir novas keys com && !
                    continue;
                }

                System.out.println("Processando: " + chave);

                try {
                    List<Incidente> incidentes = s3Service.buscarIncidentes(chave);

                    System.out.println(incidentes.size() + " incidente(s) encontrado(s) em " + chave);

                    incidentService.processarIncidentes(incidentes);

                } catch (Exception e) {
                    System.err.println("Erro ao processar " + chave + ": " + e.getMessage());
                }
            }

            // Verifica se algum incidente aberto foi resolvido no Jira
            System.out.println("Verificando incidentes resolvidos no Jira...");
            incidentService.verificarResolvidos();

            System.out.println("Varredura concluída.");

        } catch (Exception e) {
            System.err.println("Erro na varredura S3: " + e.getMessage());
            e.printStackTrace();
        }
    }
}