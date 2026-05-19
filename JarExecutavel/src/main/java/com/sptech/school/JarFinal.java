package com.sptech.school;
import com.sun.source.tree.BreakTree;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

public class JarFinal {

    String nivelChamado = severidades[0];
    String nivelStatus = status[0];
    String evento;
    String componente;
    String servidor;

    public static final String[] severidades = {"Highest","High","Medium", "Low"};
    public static final String[] status = {"Online", "Offline", "Atenção", "Crítico"};
    public static final String[] componentes = {"CPU", "RAM", "Disco", "Rede", "Temperatura", "Processos"};
    public static final String[] servidores = {"srv-bd-01", "srv-app-02", "srv-atc-03"};

    public String logHardware() {
        Random random = new Random();
        double valorComponente = random.nextDouble(0, 101);
        double limite = 70.0;
        boolean gerarIndidente = false;

        DateTimeFormatter data_hora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String tempoReal = LocalDateTime.now().format(data_hora);
        componente = componentes[random.nextInt(componentes.length)];
        servidor = servidores[random.nextInt(servidores.length)];

        if (valorComponente == 0){
            // servidor offline ou sem conexão
            nivelStatus = status[1]; // offline
            nivelChamado = severidades[0]; // crítica
            gerarIndidente = true;
        } else if (valorComponente >= (limite * 0.7) && valorComponente < (limite * 0.8)){
            // componente acima do limite em 70% e menor que 80% do limite
            nivelStatus = status[0]; // online
            nivelChamado = severidades[3]; // baixa
            gerarIndidente = true;
        } else if (valorComponente >= (limite * 0.8) && valorComponente < (limite * 0.9)){
            // componente acima do limite em 80% e abaixo de 90%
            nivelStatus = status[2]; // atenção
            nivelChamado = severidades[2]; // média
            gerarIndidente = true;
        } else if (valorComponente >= (limite * 0.9) && valorComponente < limite) {
            // componente acima do limite em 90% e baixo de 100%
            nivelStatus = status[3]; // crítico
            nivelChamado = severidades[1]; // alta
            gerarIndidente = true;
        } else if (valorComponente >= limite){
            nivelStatus = status[3]; // crítico
            nivelChamado = severidades[0]; // crítica
            gerarIndidente = true;
        }

        if(gerarIndidente){
            String valorFormatado = String.format("%.2f", valorComponente);

            String mensagem = "Severidade do incidente: " + nivelChamado + "\n" + servidor + "\nStatus do servidor: " +
                    nivelStatus + "\n" + componente + " está com " + valorFormatado + "% de uso\n";

            evento = componente + " está com " + valorFormatado + "% de uso";
            return mensagem;
        }
        return null;
    }

    public String getEvento() {
        return evento;
    }

    public String getNivelChamado() {
        return nivelChamado;
    }

    public String getComponente() {
        return componente;
    }

    public String getServidor() {
        return servidor;
    }
}

