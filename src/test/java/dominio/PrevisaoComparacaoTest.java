package dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PrevisaoComparacaoTest {

    private List<Medicao> criarHistorico(double[] valores) {
        List<Medicao> historico = new ArrayList<>();
        for (double valor : valores) {
            historico.add(new Medicao(0, "TEMPERATURA", "°C", "Região Norte", valor, LocalDateTime.now()));
        }
        return historico;
    }

    @Test
    public void devePreverAltaQuandoValoresEstaoSubindo() {
        // Arrange
        PrevisaoComparacao previsao = new PrevisaoComparacao(10);
        List<Medicao> historico = criarHistorico(new double[] { 30, 30, 30, 30, 30, 34, 34, 34, 34, 34 });

        // Act
        double resultado = previsao.prever(historico);

        // Assert: subiu 4 de um periodo para o outro, entao preve 34 + 4
        assertEquals(38.0, resultado);
    }

    @Test
    public void devePreverQuedaQuandoValoresEstaoCaindo() {
        // Arrange
        PrevisaoComparacao previsao = new PrevisaoComparacao(10);
        List<Medicao> historico = criarHistorico(new double[] { 40, 40, 40, 40, 40, 36, 36, 36, 36, 36 });

        // Act
        double resultado = previsao.prever(historico);

        // Assert: caiu 4, entao preve 36 - 4
        assertEquals(32.0, resultado);
    }
}