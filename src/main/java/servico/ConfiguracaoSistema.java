package servico;

import dominio.DadosTerritorio;
import excecao.DadosInvalidosException;

public class ConfiguracaoSistema {

    private static ConfiguracaoSistema instancia;

    private DadosTerritorio dadosTerritorio;
    private int minimoLeituras = 5;
    private int leiturasMediaMovel = 5;
    private int leiturasComparacao = 10;

    // construtor privado: ninguem de fora consegue dar new ConfiguracaoSistema()
    private ConfiguracaoSistema() {
        try {
            dadosTerritorio = new DadosTerritorio("Território Monitorado", 45000);
        } catch (DadosInvalidosException e) {
            throw new IllegalStateException("Dados iniciais do território inválidos: " + e.getMessage());
        }
    }

    public static ConfiguracaoSistema getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracaoSistema();
        }
        return instancia;
    }

    public DadosTerritorio getDadosTerritorio() {
        return dadosTerritorio;
    }

    public int getMinimoLeituras() {
        return minimoLeituras;
    }

    public int getLeiturasMediaMovel() {
        return leiturasMediaMovel;
    }

    public int getLeiturasComparacao() {
        return leiturasComparacao;
    }
}