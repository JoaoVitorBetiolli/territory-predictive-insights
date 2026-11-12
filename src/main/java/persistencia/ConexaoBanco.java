package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexaoBanco {

    private static final String URL = "jdbc:h2:./dados/territorio";
    private static final String USUARIO = "sa";
    private static final String SENHA = "";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void criarTabelas() throws SQLException {
        String sqlMedicao = "CREATE TABLE IF NOT EXISTS medicao ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "tipo_sensor VARCHAR(30) NOT NULL, "
                + "unidade VARCHAR(10) NOT NULL, "
                + "regiao VARCHAR(50) NOT NULL, "
                + "valor DOUBLE NOT NULL, "
                + "data_hora TIMESTAMP NOT NULL)";

        String sqlAlerta = "CREATE TABLE IF NOT EXISTS alerta ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "tipo_sensor VARCHAR(30) NOT NULL, "
                + "regiao VARCHAR(50) NOT NULL, "
                + "nivel VARCHAR(10) NOT NULL, "
                + "valor_previsto DOUBLE NOT NULL, "
                + "limite DOUBLE NOT NULL, "
                + "explicacao VARCHAR(500) NOT NULL, "
                + "data_hora TIMESTAMP NOT NULL)";

        try (Connection conexao = getConexao();
                PreparedStatement psMedicao = conexao.prepareStatement(sqlMedicao);
                PreparedStatement psAlerta = conexao.prepareStatement(sqlAlerta)) {
            psMedicao.execute();
            psAlerta.execute();
        }
    }
}