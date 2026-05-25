package com.sptech.school.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Criticidade {
    CRITICO,
    ALTO,
    MEDIO,
    BAIXO;

    Criticidade() {
    }

    @JsonCreator
    public static Criticidade from(String value) {
        if (value == null) return null;

        try {
            return Criticidade.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            return BAIXO; // ou DEFAULT de sua regra
        }
    }

}
