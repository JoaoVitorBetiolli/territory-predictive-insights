package dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import excecao.DadosInvalidosException;

public class MedicaoTest {

    @Test
    public void deveCriarMedicaoComValorDentroDaFaixa() throws DadosInvalidosException {
        // Arrange
        Sensor sensor = new SensorTemperatura();

        // Act
        Medicao medicao = new Medicao(sensor, "Região Norte", 30.0);

        // Assert
        assertEquals(30.0, medicao.getValor());
        assertEquals("TEMPERATURA", medicao.getTipoSensor());
        assertEquals("Região Norte", medicao.getRegiao());
    }

    @Test
    public void deveLancarExcecaoQuandoValorPassaDaFaixa() {
        // Arrange
        Sensor sensor = new SensorTemperatura();

        // Act + Assert
        assertThrows(DadosInvalidosException.class, () -> new Medicao(sensor, "Região Norte", 80.0));
    }

    @Test
    public void deveLancarExcecaoQuandoRegiaoEstaVazia() {
        // Arrange
        Sensor sensor = new SensorTemperatura();

        // Act + Assert
        assertThrows(DadosInvalidosException.class, () -> new Medicao(sensor, "", 30.0));
    }
}