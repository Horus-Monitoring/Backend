package com.sptech.school.service;

import com.sptech.school.config.S3Connection;
import com.sptech.school.config.S3Provider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class S3Service {

    private static final S3Client client =
            S3Provider.criarCliente();

    private static final String bucket =
            S3Connection.getBUCKET_NAME();


    public static void uploadArquivo(String caminhoLocal,
                                     String chaveS3) {

        try {

            File arquivo = new File(caminhoLocal);

            PutObjectRequest request =
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(chaveS3)
                            .build();

            client.putObject(
                    request,
                    RequestBody.fromFile(arquivo)
            );

            System.out.println(
                    "Upload realizado com sucesso!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao fazer upload: "
                            + e.getMessage()
            );
        }
    }


    public static void uploadTexto(String conteudo,
                                   String chaveS3) {

        try {

            PutObjectRequest request =
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(chaveS3)
                            .build();

            client.putObject(
                    request,
                    RequestBody.fromString(conteudo)
            );

            System.out.println(
                    "Texto enviado com sucesso!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao enviar texto: "
                            + e.getMessage()
            );
        }
    }


    public static void baixarArquivo(String chaveS3,
                                     String destinoLocal) {

        try {

            GetObjectRequest request =
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(chaveS3)
                            .build();

            client.getObject(
                    request,
                    Paths.get(destinoLocal)
            );

            System.out.println(
                    "Arquivo baixado com sucesso!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao baixar arquivo: "
                            + e.getMessage()
            );
        }
    }


    public static void lerArquivo(String chaveS3) {

        try {

            GetObjectRequest request =
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(chaveS3)
                            .build();

            ResponseBytes<GetObjectResponse> objeto =
                    client.getObjectAsBytes(request);

            String conteudo =
                    objeto.asString(StandardCharsets.UTF_8);

            System.out.println(conteudo);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao ler arquivo: "
                            + e.getMessage()
            );
        }
    }


    public static void listarArquivos(String prefixo) {

        try {

            ListObjectsV2Request request =
                    ListObjectsV2Request.builder()
                            .bucket(bucket)
                            .prefix(prefixo)
                            .build();

            ListObjectsV2Response response =
                    client.listObjectsV2(request);

            for (S3Object objeto : response.contents()) {

                System.out.println(
                        objeto.key()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro ao listar arquivos: "
                            + e.getMessage()
            );
        }
    }


    public static void arquivoExiste(String chaveS3) {

        try {

            HeadObjectRequest request =
                    HeadObjectRequest.builder()
                            .bucket(bucket)
                            .key(chaveS3)
                            .build();

            client.headObject(request);

            System.out.println(
                    "Arquivo existe!"
            );

        } catch (NoSuchKeyException e) {

            System.out.println(
                    "Arquivo não encontrado!"
            );

        } catch (S3Exception e) {

            if (e.statusCode() == 404) {

                System.out.println(
                        "Arquivo não encontrado!"
                );

            } else {

                System.out.println(
                        "Erro ao verificar arquivo: "
                                + e.getMessage()
                );
            }
        }
    }
}