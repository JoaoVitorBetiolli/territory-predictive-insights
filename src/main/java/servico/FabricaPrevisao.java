package servico;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dominio.EstrategiaPrevisao;
import dominio.PrevisaoComparacao;
import dominio.PrevisaoMediaMovel;

public class FabricaPrevisao {

    private Map<String, EstrategiaPrevisao> estrategias = new LinkedHashMap<>();

    public FabricaPrevisao() {
        ConfiguracaoSistema config = ConfiguracaoSistema.getInstancia();
        estrategias.put("MEDIA_MOVEL", new PrevisaoMediaMovel(config.getLeiturasMediaMovel()));
        estrategias.put("COMPARACAO", new PrevisaoComparacao(config.getLeiturasComparacao()));
    }

    public EstrategiaPrevisao criar(String tipo) {
        EstrategiaPrevisao estrategia = estrategias.get(tipo);
        if (estrategia == null) {
            throw new IllegalArgumentException("Estratégia de previsão desconhecida: " + tipo);
        }
        return estrategia;
    }

    public List<String> getTiposDisponiveis() {
        return new ArrayList<>(estrategias.keySet());
    }
}