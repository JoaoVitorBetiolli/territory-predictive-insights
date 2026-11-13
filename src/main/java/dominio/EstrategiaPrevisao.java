package dominio;

import java.util.List;

public abstract class EstrategiaPrevisao {

    private String nome;
    private int quantidadeLeituras;

    public EstrategiaPrevisao(String nome, int quantidadeLeituras) {
        this.nome = nome;
        this.quantidadeLeituras = quantidadeLeituras;
    }

    public abstract double prever(List<Medicao> historico);

    public abstract String getDescricao();

    public String getNome() {
        return nome;
    }

    public int getQuantidadeLeituras() {
        return quantidadeLeituras;
    }

    protected double calcularMedia(List<Medicao> medicoes) {
        if (medicoes.isEmpty()) {
            throw new IllegalArgumentException("Não há medições para calcular a média.");
        }

        double soma = 0;
        for (Medicao medicao : medicoes) {
            soma += medicao.getValor();
        }
        return soma / medicoes.size();
    }

    protected double arredondar(double valor) {
        return Math.round(valor * 10) / 10.0;
    }
}