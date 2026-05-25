package com.sptech.school.app;

import com.sptech.school.model.Incidente;
import com.sptech.school.service.IncidentService;
import com.sptech.school.service.S3Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import com.sptech.school.provider.S3Provider;
import com.sptech.school.config.S3Connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = "com.sptech.school")
public class App {

    public static void main(String[] args) throws Exception {
        // Sobe o Spring (porta 8080) + daemon juntos
        SpringApplication.run(App.class, args);

        System.out.println("Iniciando daemon de monitoramento S3...");
        modoDaemon();
    }

    private static void modoDaemon() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(
                App::escanearS3,
                10,
                60,
                TimeUnit.MINUTES
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Encerrando daemon...");
            scheduler.shutdownNow();
        }));
    }

    private static void escanearS3() {
        System.out.println("Iniciando varredura S3 em: " + java.time.LocalDateTime.now());

        try {
            S3Client s3      = S3Provider.criarCliente();
            String   bucket  = S3Connection.getBUCKET_NAME();
            String   prefixo = "client/alertas/";

            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .prefix(prefixo)
                    .build();

            ListObjectsV2Response listResponse = s3.listObjectsV2(listRequest);

            S3Service       s3Service       = new S3Service();
            IncidentService incidentService = new IncidentService();

            for (S3Object objeto : listResponse.contents()) {
                String chave = objeto.key();

                if (!chave.endsWith("incidentes_rede_24h.json")) {
                    continue;
                }

                System.out.println("Processando: " + chave);

                try {
                    List<Incidente> incidentes = s3Service.buscarIncidentes(chave);
                    incidentService.processarIncidentes(incidentes);
                } catch (Exception e) {
                    System.err.println("Erro ao processar " + chave + ": " + e.getMessage());
                }
            }

            incidentService.verificarResolvidos();
            System.out.println("Varredura concluída.");

        } catch (Exception e) {
            System.err.println("Erro na varredura S3:");
            e.printStackTrace();
        }
    }
}