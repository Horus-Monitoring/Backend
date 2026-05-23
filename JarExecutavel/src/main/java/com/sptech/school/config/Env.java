package com.sptech.school.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {

    private static final Dotenv dotenv = Dotenv.configure()
            .filename(".env.dev").directory(System.getProperty("user.dir")).load();

    public static String get(String key) {
        String val = System.getenv(key);
        if (val != null && !val.isBlank()) return val;

        val = dotenv.get(key);
        return val;
    }
}