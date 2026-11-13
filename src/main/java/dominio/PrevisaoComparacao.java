package dominio;

import java.util.List;

public class PrevisaoComparacao extends EstrategiaPrevisao {

    public PrevisaoComparacao(int quantidadeLeituras) {
        super("Comparação com período anterior", quantidadeLeituras);
    }

    // divide o historico em duas metades: periodo anterior e periodo recente.
    // se o recente subiu em relacao ao anterior, a previsao continua subindo no mesmo ritmo
    @Override
    public double prever(List<Medicao> historico) {
        int inicio = Math.max(0, historico.size() - getQuantidadeLeituras());
        List<Medicao> ultimas = historico.subList(inicio, historico.size());

        int metade = ultimas.size() / 2;
        double mediaAnterior = calcularMedia(ultimas.subList(0, metade));
        double mediaRecente = calcularMedia(ultimas.subList(metade, ultimas.size()));

        double variacao = mediaRecente - mediaAnterior;
        return arredondar(mediaRecente + variacao);
    }

    @Override
    public String getDescricao() {
        return "tendência entre o período recente e o anterior (últimas " + getQuantidadeLeituras() + " leituras)";
    }
}