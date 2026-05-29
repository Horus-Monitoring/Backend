package com.sptech.school.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static final String HOST = Env.get("DB_HOST");
    private static final String PORT = Env.get("DB_PORT");
    private static final String DB   = Env.get("DB_DATABASE");
    private static final String USER = Env.get("DB_USER");
    private static final String PASSWORD = Env.get("DB_PASSWORD");

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB
                    + "?useUnicode=true"
                    + "&characterEncoding=UTF-8"
                    + "&connectionCollation=utf8mb4_unicode_ci"
                    + "&serverTimezone=UTC"
                    + "&allowPublicKeyRetrieval=true"
                    + "&useSSL=false";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}