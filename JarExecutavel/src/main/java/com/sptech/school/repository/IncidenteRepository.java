package com.sptech.school.repository;

import com.sptech.school.config.MySQLConnection;
import com.sptech.school.model.Incidente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidenteRepository {

    // Agora verifica pela 'chave' (que é a sua JiraKey)
    public boolean existe(String chave){

        String sql = """
                SELECT id_registro_alerta
                FROM registro_alerta
                WHERE chave = ?
                """;

        try(
                Connection conn = MySQLConnection.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){

            ps.setString(1, chave);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar incidente: "
                            + e.getMessage()
            );
        }
    }

    public void salvar(Incidente incidente){

        String sql = """
                INSERT INTO registro_alerta (
                    chave,
                    titulo,
                    status_alerta,
                    criticidade,
                    data_alerta,
                    fk_servidor,
                    fk_componente
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try(
                Connection conn = MySQLConnection.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){

            ps.setString(1, incidente.getChave()); // Chave é a JiraKey
            ps.setString(2, incidente.getTitulo());

            // Se o statusAlerta vier nulo, define como "Ativo" por padrão
            ps.setString(3, incidente.getStatusAlerta() != null ? incidente.getStatusAlerta() : "Ativo");
            ps.setString(4, incidente.getCriticidade());

            ps.setTimestamp(
                    5,
                    Timestamp.valueOf(
                            incidente.getDataAlerta()
                    )
            );

            // Verificação de segurança para as Foreign Keys (para evitar NullPointerException)
            if (incidente.getFkServidor() != null) {
                ps.setInt(6, incidente.getFkServidor());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (incidente.getFkComponente() != null) {
                ps.setInt(7, incidente.getFkComponente());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar incidente: "
                            + e.getMessage()
            );
        }
    }

    public List<Incidente> buscarAbertos(){

        List<Incidente> incidentes = new ArrayList<>();

        String sql = """
                SELECT *
                FROM registro_alerta
                WHERE status_alerta != 'Resolvido'
                """;

        try(
                Connection conn = MySQLConnection.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Incidente incidente = new Incidente();

                incidente.setIdRegistroAlerta(
                        rs.getInt("id_registro_alerta")
                );

                incidente.setChave(
                        rs.getString("chave") // Chave é a JiraKey
                );

                incidente.setTitulo(
                        rs.getString("titulo")
                );

                incidente.setStatusAlerta(
                        rs.getString("status_alerta")
                );

                incidente.setCriticidade(
                        rs.getString("criticidade")
                );

                Timestamp dataAlerta = rs.getTimestamp("data_alerta");
                if (dataAlerta != null) {
                    incidente.setDataAlerta(dataAlerta.toLocalDateTime());
                }

                Timestamp dataResolucao = rs.getTimestamp("data_resolucao");
                if (dataResolucao != null) {
                    incidente.setDataResolucao(dataResolucao.toLocalDateTime());
                }

                incidente.setFkServidor(
                        rs.getInt("fk_servidor")
                );

                incidente.setFkComponente(
                        rs.getInt("fk_componente")
                );

                incidentes.add(incidente);
            }

            return incidentes;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar incidentes: "
                            + e.getMessage()
            );
        }
    }

    // Recebe a 'chave' (JiraKey) para atualizar o status do alerta
    public void marcarResolvido(String chave){

        String sql = """
                UPDATE registro_alerta
                SET status_alerta = 'Resolvido',
                    data_resolucao = CURRENT_TIMESTAMP
                WHERE chave = ?
                """;

        try(
                Connection conn = MySQLConnection.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){

            ps.setString(1, chave);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao atualizar incidente: "
                            + e.getMessage()
            );
        }
    }
}