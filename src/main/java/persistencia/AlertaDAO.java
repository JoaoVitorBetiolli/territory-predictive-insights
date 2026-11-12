package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dominio.Alerta;
import dominio.NivelAlerta;

public class AlertaDAO {

    public void salvar(Alerta alerta) throws SQLException {
        String sql = "INSERT INTO alerta (tipo_sensor, regiao, nivel, valor_previsto, limite, explicacao, data_hora) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, alerta.getTipoSensor());
            ps.setString(2, alerta.getRegiao());
            ps.setString(3, alerta.getNivel().name());
            ps.setDouble(4, alerta.getValorPrevisto());
            ps.setDouble(5, alerta.getLimite());
            ps.setString(6, alerta.getExplicacao());
            ps.setTimestamp(7, Timestamp.valueOf(alerta.getDataHora()));
            ps.executeUpdate();
        }
    }

    public List<Alerta> listarTodos() throws SQLException {
        String sql = "SELECT * FROM alerta ORDER BY data_hora DESC";
        List<Alerta> lista = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Alerta(
                        rs.getInt("id"),
                        rs.getString("tipo_sensor"),
                        rs.getString("regiao"),
                        NivelAlerta.valueOf(rs.getString("nivel")),
                        rs.getDouble("valor_previsto"),
                        rs.getDouble("limite"),
                        rs.getString("explicacao"),
                        rs.getTimestamp("data_hora").toLocalDateTime()));
            }
        }
        return lista;
    }
}