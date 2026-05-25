package com.sptech.school.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {

    private static final Dotenv dotenv = Dotenv.configure()
            .filename(".env.dev")
            .ignoreIfMissing()
            .load();

    public static String get(String key) {

        String value = System.getenv(key);

        if (value != null && !value.isBlank()) {
            return value;
        }

        return dotenv.get(key);
    }
}