package dominio;

import java.util.List;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {

    public PrevisaoMediaMovel(int quantidadeLeituras) {
        super("Média móvel", quantidadeLeituras);
    }

    // a previsao e a media das ultimas N leituras
    @Override
    public double prever(List<Medicao> historico) {
        int inicio = Math.max(0, historico.size() - getQuantidadeLeituras());
        List<Medicao> ultimas = historico.subList(inicio, historico.size());
        return arredondar(calcularMedia(ultimas));
    }

    @Override
    public String getDescricao() {
        return "média das últimas " + getQuantidadeLeituras() + " leituras";
    }
}