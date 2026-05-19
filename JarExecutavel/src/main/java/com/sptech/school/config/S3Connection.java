package com.sptech.school.config;

import io.github.cdimascio.dotenv.Dotenv;


public class S3Connection {
    private static final String ACCESS_KEY =  Dotenv.configure().filename(".env.dev").load().get("AWS_ACCESS_KEY_ID");
    private static final String SECRET_ACCESS_KEY =Dotenv.configure().filename(".env.dev").load().get("AWS_SECRET_ACCESS_KEY");
    private static final String SESSION_TOKEN =Dotenv.configure().filename(".env.dev").load().get("AWS_SESSION_TOKEN");
    private static final String REGION = Dotenv.configure().filename(".env.dev").load().get("AWS_REGION");
    private static final String BUCKET_NAME = Dotenv.configure().filename(".env.dev").load().get("AWS_BUCKET_NAME");

    public static String getACCESS_KEY() {
        return ACCESS_KEY;
    }

    public static String getSECRET_ACCESS_KEY() {
        return SECRET_ACCESS_KEY;
    }

    public static String getSESSION_TOKEN() {
        return SESSION_TOKEN;
    }

    public static String getREGION() {
        return REGION;
    }

    public static String getBUCKET_NAME() {
        return BUCKET_NAME;
    }
}
