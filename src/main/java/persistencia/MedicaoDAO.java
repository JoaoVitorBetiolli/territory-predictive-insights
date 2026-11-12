package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dominio.Medicao;

public class MedicaoDAO {

    public void salvar(Medicao medicao) throws SQLException {
        String sql = "INSERT INTO medicao (tipo_sensor, unidade, regiao, valor, data_hora) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, medicao.getTipoSensor());
            ps.setString(2, medicao.getUnidade());
            ps.setString(3, medicao.getRegiao());
            ps.setDouble(4, medicao.getValor());
            ps.setTimestamp(5, Timestamp.valueOf(medicao.getDataHora()));
            ps.executeUpdate();
        }
    }

    public List<Medicao> buscarHistoricoPorSensor(String tipoSensor) throws SQLException {
        String sql = "SELECT * FROM medicao WHERE tipo_sensor = ? ORDER BY data_hora";
        List<Medicao> lista = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, tipoSensor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarMedicao(rs));
                }
            }
        }
        return lista;
    }

    // busca as ultimas leituras de um sensor em uma regiao, da mais antiga para a mais nova
    public List<Medicao> buscarUltimas(String tipoSensor, String regiao, int quantidade) throws SQLException {
        String sql = "SELECT * FROM medicao WHERE tipo_sensor = ? AND regiao = ? ORDER BY data_hora DESC, id DESC LIMIT ?";
        List<Medicao> lista = new ArrayList<>();

        try (Connection conexao = ConexaoBanco.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, tipoSensor);
            ps.setString(2, regiao);
            ps.setInt(3, quantidade);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarMedicao(rs));
                }
            }
        }

        Collections.reverse(lista);
        return lista;
    }

    private Medicao montarMedicao(ResultSet rs) throws SQLException {
        return new Medicao(
                rs.getInt("id"),
                rs.getString("tipo_sensor"),
                rs.getString("unidade"),
                rs.getString("regiao"),
                rs.getDouble("valor"),
                rs.getTimestamp("data_hora").toLocalDateTime());
    }
}