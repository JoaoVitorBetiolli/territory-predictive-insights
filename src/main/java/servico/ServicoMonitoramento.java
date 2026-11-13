package servico;

import java.sql.SQLException;
import java.util.List;

import dominio.Alerta;
import dominio.EstrategiaPrevisao;
import dominio.Medicao;
import dominio.NivelAlerta;
import persistencia.AlertaDAO;
import persistencia.MedicaoDAO;

public class ServicoMonitoramento {

    private MedicaoDAO medicaoDAO;
    private AlertaDAO alertaDAO;
    private FabricaPrevisao fabricaPrevisao;

    // os DAOs vem de fora para que os testes possam passar um mock no lugar
    public ServicoMonitoramento(MedicaoDAO medicaoDAO, AlertaDAO alertaDAO, FabricaPrevisao fabricaPrevisao) {
        this.medicaoDAO = medicaoDAO;
        this.alertaDAO = alertaDAO;
        this.fabricaPrevisao = fabricaPrevisao;
    }

    public String rodarPrevisao(String tipoEstrategia, String tipoSensor, String regiao) throws SQLException {
        ConfiguracaoSistema config = ConfiguracaoSistema.getInstancia();
        EstrategiaPrevisao estrategia = fabricaPrevisao.criar(tipoEstrategia);

        List<Medicao> historico = medicaoDAO.buscarUltimas(tipoSensor, regiao, estrategia.getQuantidadeLeituras());

        // com poucas leituras a previsao nao e confiavel, entao o sistema nao preve
        if (historico.size() < config.getMinimoLeituras()) {
            return regiao + " | " + tipoSensor + ": só existem " + historico.size()
                    + " leituras no banco. São necessárias pelo menos " + config.getMinimoLeituras()
                    + " para uma previsão confiável.";
        }

        double previsto = estrategia.prever(historico);
        String unidade = historico.get(0).getUnidade();
        double limite = config.getDadosTerritorio().getLimitePorTipo(tipoSensor);
        NivelAlerta nivel = calcularNivel(previsto, limite);

        String resultado = regiao + " | " + tipoSensor + ": previsto " + previsto + " " + unidade + " ("
                + estrategia.getDescricao() + ")";

        if (nivel == null) {
            return resultado + " -> dentro do limite de " + limite + " " + unidade + ", sem alerta.";
        }

        String explicacao = "Sensor " + tipoSensor + " na " + regiao + ": valor previsto de " + previsto + " "
                + unidade + " " + descreverMotivo(nivel) + " " + limite + " " + unidade + ".";

        Alerta alerta = new Alerta(tipoSensor, regiao, nivel, previsto, limite, explicacao);
        alertaDAO.salvar(alerta);

        return resultado + " -> [" + nivel.getTexto() + "] " + explicacao;
    }

    // devolve null quando a previsao esta abaixo de 90% do limite
    public NivelAlerta calcularNivel(double previsto, double limite) {
        if (previsto >= limite * 1.10) {
            return NivelAlerta.CRITICO;
        }
        if (previsto >= limite) {
            return NivelAlerta.ALERTA;
        }
        if (previsto >= limite * 0.90) {
            return NivelAlerta.ATENCAO;
        }
        return null;
    }

    private String descreverMotivo(NivelAlerta nivel) {
        switch (nivel) {
            case CRITICO:
                return "passou em mais de 10% o limite de";
            case ALERTA:
                return "passou o limite de";
            default:
                return "está a menos de 10% do limite de";
        }
    }
}