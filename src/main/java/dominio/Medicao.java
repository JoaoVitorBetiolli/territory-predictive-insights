package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import excecao.DadosInvalidosException;

public class Medicao {

    private int id;
    private String tipoSensor;
    private String unidade;
    private String regiao;
    private double valor;
    private LocalDateTime dataHora;

    // usado quando o sensor faz uma leitura nova
    public Medicao(Sensor sensor, String regiao, double valor) throws DadosInvalidosException {
        if (valor < sensor.getValorMinimo() || valor > sensor.getValorMaximo()) {
            throw new DadosInvalidosException("Leitura de " + sensor.getTipo() + " inválida: " + valor + " "
                    + sensor.getUnidade() + " está fora da faixa de " + sensor.getValorMinimo() + " a "
                    + sensor.getValorMaximo() + ".");
        }
        if (regiao == null || regiao.isBlank()) {
            throw new DadosInvalidosException("A medição precisa ter uma região.");
        }

        this.tipoSensor = sensor.getTipo();
        this.unidade = sensor.getUnidade();
        this.regiao = regiao;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
    }

    // usado quando a medição vem do banco de dados
    public Medicao(int id, String tipoSensor, String unidade, String regiao, double valor, LocalDateTime dataHora) {
        this.id = id;
        this.tipoSensor = tipoSensor;
        this.unidade = unidade;
        this.regiao = regiao;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public String getUnidade() {
        return unidade;
    }

    public String getRegiao() {
        return regiao;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataHora.format(formato) + " | " + regiao + " | " + tipoSensor + ": " + valor + " " + unidade;
    }
}