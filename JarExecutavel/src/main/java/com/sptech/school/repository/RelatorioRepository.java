package com.sptech.school.repository;

import com.sptech.school.config.MySQLConnection;
import com.sptech.school.model.Relatorio;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioRepository {
    public List<Relatorio> buscarDados(String usuario, String hostname) throws SQLException {
        List<Relatorio> dadosUsuario = new ArrayList<>();

        String mysql = """
                SELECT f.nome,
                		f.cpf,
                        f.funcao,
                        f.email,
                        e.razao_social,
                        s.hostname,
                        s.mac_address,
                        s.status_servidor,
                        c.tipo,
                        sc.limite,
                        sc.unidade_medida,
                        ra.data_alerta,
                        ra.criticidade,
                        ra.status_alerta
                        FROM funcionario AS f
                        JOIN empresa AS e
                             ON e.id_empresa = f.fk_empresa
                        JOIN servidor AS s
                             ON s.fk_empresa = e.id_empresa
                        JOIN servidor_componente AS sc
                             ON sc.fk_servidor = s.id_servidor
                        JOIN componente AS c
                             ON c.id_componente = sc.fk_componente
                        LEFT JOIN registro_alerta AS ra
                             ON ra.fk_componente = c.id_componente
                            AND ra.fk_servidor = s.id_servidor
                        WHERE f.email = ? 
                            AND s.hostname = ?;
                """;

        try (Connection conexao = MySQLConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(mysql);
        ){

            ps.setString(1, usuario);
            ps.setString(2, hostname);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Timestamp dataAlertaTs = rs.getTimestamp("data_alerta");
                LocalDateTime dataAlerta = (dataAlertaTs != null) ? dataAlertaTs.toLocalDateTime() : null;

                String criticidade = rs.getString("criticidade");
                String statusAlerta = rs.getString("status_alerta");

                Relatorio data = new Relatorio(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("funcao"),
                        rs.getString("email"),
                        rs.getString("razao_social"),
                        rs.getString("hostname"),
                        rs.getString("mac_address"),
                        rs.getString("status_servidor"),
                        rs.getString("tipo"),
                        rs.getDouble("limite"),
                        rs.getString("unidade_medida"),
                        dataAlerta, //Tratamento para ausencia de alertas
                        criticidade != null ? criticidade : "Sem alertas",
                        statusAlerta != null ? statusAlerta : "N/A"

                );
                dadosUsuario.add(data);

            }
            return dadosUsuario;
        } catch (SQLException e) {
            throw new SQLException("Erro ao capturar os dados do MySQL:" + e);
        }
    }
}

