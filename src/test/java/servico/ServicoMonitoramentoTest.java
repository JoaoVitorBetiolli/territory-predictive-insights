package servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dominio.Alerta;
import dominio.Medicao;
import dominio.NivelAlerta;
import persistencia.AlertaDAO;
import persistencia.MedicaoDAO;

public class ServicoMonitoramentoTest {

    private MedicaoDAO medicaoDAO;
    private AlertaDAO alertaDAO;
    private ServicoMonitoramento servico;

    // roda antes de cada teste: cria DAOs "falsos", que nao usam o banco
    @BeforeEach
    public void prepararMocks() {
        medicaoDAO = mock(MedicaoDAO.class);
        alertaDAO = mock(AlertaDAO.class);
        servico = new ServicoMonitoramento(medicaoDAO, alertaDAO, new FabricaPrevisao());
    }

    private List<Medicao> criarHistorico(double[] valores) {
        List<Medicao> historico = new ArrayList<>();
        for (double valor : valores) {
            historico.add(new Medicao(0, "TEMPERATURA", "°C", "Região Norte", valor, LocalDateTime.now()));
        }
        return historico;
    }

    @Test
    public void deveGerarAlertaCriticoQuandoPrevisaoPassaMuitoDoLimite() throws Exception {
        // Arrange: o DAO falso devolve 5 leituras de 40 graus (limite padrao e 35)
        List<Medicao> historico = criarHistorico(new double[] { 40, 40, 40, 40, 40 });
        when(medicaoDAO.buscarUltimas("TEMPERATURA", "Região Norte", 5)).thenReturn(historico);

        // Act
        String resultado = servico.rodarPrevisao("MEDIA_MOVEL", "TEMPERATURA", "Região Norte");

        // Assert: o alerta foi salvo e e critico
        verify(alertaDAO).salvar(any(Alerta.class));
        assertTrue(resultado.contains("CRÍTICO"));
    }

    @Test
    public void naoDeveGerarAlertaComMenosDeCincoLeituras() throws Exception {
        // Arrange: so 3 leituras no historico
        List<Medicao> historico = criarHistorico(new double[] { 40, 40, 40 });
        when(medicaoDAO.buscarUltimas("TEMPERATURA", "Região Norte", 5)).thenReturn(historico);

        // Act
        String resultado = servico.rodarPrevisao("MEDIA_MOVEL", "TEMPERATURA", "Região Norte");

        // Assert: o sistema se cala e nenhum alerta e salvo
        verify(alertaDAO, never()).salvar(any(Alerta.class));
        assertTrue(resultado.contains("São necessárias pelo menos 5"));
    }

    @Test
    public void naoDeveGerarAlertaQuandoPrevisaoEstaAbaixoDoLimite() throws Exception {
        // Arrange
        List<Medicao> historico = criarHistorico(new double[] { 25, 26, 27, 26, 25 });
        when(medicaoDAO.buscarUltimas("TEMPERATURA", "Região Norte", 5)).thenReturn(historico);

        // Act
        String resultado = servico.rodarPrevisao("MEDIA_MOVEL", "TEMPERATURA", "Região Norte");

        // Assert
        verify(alertaDAO, never()).salvar(any(Alerta.class));
        assertTrue(resultado.contains("sem alerta"));
    }

    @Test
    public void deveClassificarONivelDeAcordoComOLimite() {
        // Arrange
        double limite = 35;

        // Act + Assert
        assertNull(servico.calcularNivel(30, limite));
        assertEquals(NivelAlerta.ATENCAO, servico.calcularNivel(32, limite));
        assertEquals(NivelAlerta.ALERTA, servico.calcularNivel(36, limite));
        assertEquals(NivelAlerta.CRITICO, servico.calcularNivel(40, limite));
    }
}