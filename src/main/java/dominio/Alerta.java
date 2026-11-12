package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Alerta {

    private int id;
    private String tipoSensor;
    private String regiao;
    private NivelAlerta nivel;
    private double valorPrevisto;
    private double limite;
    private String explicacao;
    private LocalDateTime dataHora;

    // usado quando o sistema emite um alerta novo
    public Alerta(String tipoSensor, String regiao, NivelAlerta nivel, double valorPrevisto, double limite,
            String explicacao) {
        this.tipoSensor = tipoSensor;
        this.regiao = regiao;
        this.nivel = nivel;
        this.valorPrevisto = valorPrevisto;
        this.limite = limite;
        this.explicacao = explicacao;
        this.dataHora = LocalDateTime.now();
    }

    // usado quando o alerta vem do banco de dados
    public Alerta(int id, String tipoSensor, String regiao, NivelAlerta nivel, double valorPrevisto, double limite,
            String explicacao, LocalDateTime dataHora) {
        this.id = id;
        this.tipoSensor = tipoSensor;
        this.regiao = regiao;
        this.nivel = nivel;
        this.valorPrevisto = valorPrevisto;
        this.limite = limite;
        this.explicacao = explicacao;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public String getRegiao() {
        return regiao;
    }

    public NivelAlerta getNivel() {
        return nivel;
    }

    public double getValorPrevisto() {
        return valorPrevisto;
    }

    public double getLimite() {
        return limite;
    }

    public String getExplicacao() {
        return explicacao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataHora.format(formato) + " | [" + nivel.getTexto() + "] " + regiao + " | " + explicacao;
    }
}