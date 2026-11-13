package dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PrevisaoMediaMovelTest {

    private List<Medicao> criarHistorico(double[] valores) {
        List<Medicao> historico = new ArrayList<>();
        for (double valor : valores) {
            historico.add(new Medicao(0, "TEMPERATURA", "°C", "Região Norte", valor, LocalDateTime.now()));
        }
        return historico;
    }

    @Test
    public void deveCalcularMediaDasUltimasCincoLeituras() {
        // Arrange
        PrevisaoMediaMovel previsao = new PrevisaoMediaMovel(5);
        List<Medicao> historico = criarHistorico(new double[] { 10, 20, 30, 40, 50, 60 });

        // Act
        double resultado = previsao.prever(historico);

        // Assert: media de 20, 30, 40, 50 e 60
        assertEquals(40.0, resultado);
    }

    @Test
    public void deveUsarTodasAsLeiturasQuandoHistoricoEhMenorQueAJanela() {
        // Arrange
        PrevisaoMediaMovel previsao = new PrevisaoMediaMovel(5);
        List<Medicao> historico = criarHistorico(new double[] { 10, 20, 30 });

        // Act
        double resultado = previsao.prever(historico);

        // Assert
        assertEquals(20.0, resultado);
    }
}