package com.sptech.school.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;


public class MySQLConnection {
    private static final String USER = Dotenv.configure().filename(".env.dev").load().get("SQL_USER");
    private static final String PASSWORD = Dotenv.configure().filename(".env.dev").load().get("SQL_PASSWORD");
    private static final String URL = "jdbc:mysql://localhost:3306/horus_db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
