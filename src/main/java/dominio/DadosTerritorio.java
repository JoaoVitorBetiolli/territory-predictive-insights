package dominio;

import excecao.DadosInvalidosException;

public class DadosTerritorio {

    private String nome;
    private int populacao;
    private double limiteTemperatura = 35;
    private double limiteConsumoEnergia = 3000;
    private double limiteCargaRede = 90;

    public DadosTerritorio(String nome, int populacao) throws DadosInvalidosException {
        setNome(nome);
        setPopulacao(populacao);
    }

    public String getNome() {
        return nome;
    }

    public int getPopulacao() {
        return populacao;
    }

    public double getLimiteTemperatura() {
        return limiteTemperatura;
    }

    public double getLimiteConsumoEnergia() {
        return limiteConsumoEnergia;
    }

    public double getLimiteCargaRede() {
        return limiteCargaRede;
    }

    public double getLimitePorTipo(String tipoSensor) {
        switch (tipoSensor) {
            case "TEMPERATURA":
                return limiteTemperatura;
            case "CONSUMO_ENERGIA":
                return limiteConsumoEnergia;
            case "CARGA_REDE":
                return limiteCargaRede;
            default:
                throw new IllegalArgumentException("Tipo de sensor desconhecido: " + tipoSensor);
        }
    }

    public void setNome(String nome) throws DadosInvalidosException {
        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException("O nome do território não pode ficar vazio.");
        }
        this.nome = nome;
    }

    public void setPopulacao(int populacao) throws DadosInvalidosException {
        if (populacao < 0) {
            throw new DadosInvalidosException("A população não pode ser negativa: " + populacao);
        }
        this.populacao = populacao;
    }

    public void setLimiteTemperatura(double limite) throws DadosInvalidosException {
        validarLimite(limite, 55, "temperatura");
        this.limiteTemperatura = limite;
    }

    public void setLimiteConsumoEnergia(double limite) throws DadosInvalidosException {
        validarLimite(limite, 5000, "consumo de energia");
        this.limiteConsumoEnergia = limite;
    }

    public void setLimiteCargaRede(double limite) throws DadosInvalidosException {
        validarLimite(limite, 150, "carga da rede");
        this.limiteCargaRede = limite;
    }

    private void validarLimite(double limite, double maximo, String nomeLimite) throws DadosInvalidosException {
        if (limite <= 0 || limite > maximo) {
            throw new DadosInvalidosException("Limite de " + nomeLimite + " inválido: " + limite
                    + ". Use um valor maior que 0 e até " + maximo + ".");
        }
    }
}