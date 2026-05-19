package com.sptech.school.config;

import io.github.cdimascio.dotenv.Dotenv;

public class TesteEnv {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .filename(".env.dev")
                .load();

        System.out.println(
                dotenv.get("AWS_BUCKET_NAME")
        );

        System.out.println(
                dotenv.get("SQL_USER")
        );

        System.out.println(
                dotenv.get("JIRA_EMAIL")
        );

        System.out.println(
                dotenv.get("AWS_BUCKET_NAME")
        );
    }
}
