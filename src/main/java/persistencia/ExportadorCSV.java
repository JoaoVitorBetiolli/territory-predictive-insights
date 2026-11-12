package persistencia;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dominio.Medicao;

public class ExportadorCSV {

    public void exportarMedicoes(List<Medicao> medicoes, String nomeArquivo) throws IOException {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try (PrintWriter arquivo = new PrintWriter(nomeArquivo, StandardCharsets.UTF_8)) {
            // marca no inicio do arquivo para o Excel entender os acentos
            arquivo.print('\uFEFF');
            arquivo.println("data_hora;regiao;sensor;valor;unidade");

            for (Medicao medicao : medicoes) {
                String valor = String.valueOf(medicao.getValor()).replace(".", ",");
                arquivo.println(medicao.getDataHora().format(formato) + ";" + medicao.getRegiao() + ";"
                        + medicao.getTipoSensor() + ";" + valor + ";" + medicao.getUnidade());
            }
        }
    }
}