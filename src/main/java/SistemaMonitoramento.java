import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

import dominio.Alerta;
import dominio.DadosTerritorio;
import dominio.EstacaoMonitoramento;
import dominio.Medicao;
import dominio.Sensor;
import dominio.SensorCargaRede;
import dominio.SensorConsumoEnergia;
import dominio.SensorTemperatura;
import excecao.DadosInvalidosException;
import excecao.FonteIndisponivelException;
import persistencia.AlertaDAO;
import persistencia.ConexaoBanco;
import persistencia.MedicaoDAO;
import persistencia.ExportadorCSV;
import servico.ConfiguracaoSistema;

public class SistemaMonitoramento {

    private static Scanner scanner = new Scanner(System.in);
    private static MedicaoDAO medicaoDAO = new MedicaoDAO();
    private static AlertaDAO alertaDAO = new AlertaDAO();
    private static ExportadorCSV exportadorCSV = new ExportadorCSV();
    private static List<EstacaoMonitoramento> estacoes = new ArrayList<>();
    private static DadosTerritorio dadosTerritorio;

    public static void main(String[] args) {
        try {
            ConexaoBanco.criarTabelas();
        } catch (SQLException e) {
            System.out.println("Não foi possível abrir o banco de dados: " + e.getMessage());
            return;
        }

        dadosTerritorio = ConfiguracaoSistema.getInstancia().getDadosTerritorio();

        criarEstacoes();

        int opcao = -1;
        while (opcao != 0) {
            mostrarMenu();
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    verificarSensores();
                    break;
                case 2:
                    System.out.println("Previsões: em construção.");
                    break;
                case 3:
                    verHistorico();
                    break;
                case 4:
                    gerenciarDadosTerritorio();
                    break;
                case 5:
                    System.out.println("Status completo: em construção.");
                    break;
                case 0:
                    System.out.println("Encerrando o sistema.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void criarEstacoes() {
        String[] regioes = { "Região Norte", "Região Central", "Região Sul" };

        for (int i = 0; i < regioes.length; i++) {
            EstacaoMonitoramento estacao = new EstacaoMonitoramento(i + 1, "Estação " + (i + 1), regioes[i]);
            estacao.adicionarSensor(new SensorTemperatura());
            estacao.adicionarSensor(new SensorConsumoEnergia());
            estacao.adicionarSensor(new SensorCargaRede());
            estacoes.add(estacao);
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== MONITORAMENTO DE CALOR E ENERGIA =====");
        System.out.println("1 - Verificar sensores");
        System.out.println("2 - Rodar previsões");
        System.out.println("3 - Ver histórico");
        System.out.println("4 - Gerenciar dados do território");
        System.out.println("5 - Status completo");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void verificarSensores() {
        for (EstacaoMonitoramento estacao : estacoes) {
            System.out.println();
            System.out.println(estacao.getNome() + " - " + estacao.getRegiao());

            for (Sensor sensor : estacao.getSensores()) {
                try {
                    double valor = sensor.lerValor();
                    Medicao medicao = new Medicao(sensor, estacao.getRegiao(), valor);
                    medicaoDAO.salvar(medicao);
                    System.out.println("  " + sensor.getTipo() + ": " + valor + " " + sensor.getUnidade() + " (gravado)");
                } catch (FonteIndisponivelException e) {
                    System.out.println("  FALHA NO SENSOR: " + e.getMessage());
                } catch (DadosInvalidosException e) {
                    System.out.println("  LEITURA DESCARTADA: " + e.getMessage());
                } catch (SQLException e) {
                    System.out.println("  Erro ao gravar a medição no banco: " + e.getMessage());
                }
            }

            estacao.verificarStatus();
            System.out.println("  Status da estação: " + estacao.getStatus());
        }
    }

    private static void verHistorico() {
        System.out.println();
        System.out.println("1 - Medições por sensor");
        System.out.println("2 - Alertas emitidos");
        System.out.print("Escolha: ");
        int escolha = lerInteiro();

        try {
            if (escolha == 1) {
                List<Sensor> sensores = estacoes.get(0).getSensores();
                for (int i = 0; i < sensores.size(); i++) {
                    System.out.println((i + 1) + " - " + sensores.get(i).getTipo());
                }
                System.out.print("Qual sensor? ");
                int numero = lerInteiro();

                if (numero < 1 || numero > sensores.size()) {
                    System.out.println("Sensor inválido.");
                    return;
                }

                String tipo = sensores.get(numero - 1).getTipo();
                List<Medicao> historico = medicaoDAO.buscarHistoricoPorSensor(tipo);

                if (historico.isEmpty()) {
                    System.out.println("Nenhuma medição de " + tipo + " gravada ainda.");
                    return;
                }
                for (Medicao medicao : historico) {
                    System.out.println(medicao);
                }

                System.out.print("Deseja exportar esse histórico para CSV? (s/n): ");
                String resposta = scanner.nextLine().trim();
                if (resposta.equalsIgnoreCase("s")) {
                    exportarCSV(historico, tipo);
                }
            } else if (escolha == 2) {
                List<Alerta> alertas = alertaDAO.listarTodos();

                if (alertas.isEmpty()) {
                    System.out.println("Nenhum alerta emitido ainda.");
                }
                for (Alerta alerta : alertas) {
                    System.out.println(alerta);
                }
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar o histórico no banco: " + e.getMessage());
        }
    }
    
    private static void exportarCSV(List<Medicao> historico, String tipo) {
        String nomeArquivo = "historico_" + tipo + ".csv";
        try {
            exportadorCSV.exportarMedicoes(historico, nomeArquivo);
            System.out.println("Histórico exportado para o arquivo " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Não foi possível exportar o CSV: " + e.getMessage());
        }
    }

    private static void gerenciarDadosTerritorio() {
        System.out.println();
        System.out.println("Território: " + dadosTerritorio.getNome());
        System.out.println("População: " + dadosTerritorio.getPopulacao());
        System.out.println("Limite de temperatura: " + dadosTerritorio.getLimiteTemperatura() + " °C");
        System.out.println("Limite de consumo de energia: " + dadosTerritorio.getLimiteConsumoEnergia() + " kW");
        System.out.println("Limite de carga da rede: " + dadosTerritorio.getLimiteCargaRede() + " %");
        System.out.println();
        System.out.println("1 - Alterar população");
        System.out.println("2 - Alterar limite de temperatura");
        System.out.println("3 - Alterar limite de consumo de energia");
        System.out.println("4 - Alterar limite de carga da rede");
        System.out.println("0 - Voltar");
        System.out.print("Escolha: ");
        int escolha = lerInteiro();

        try {
            switch (escolha) {
                case 1:
                    System.out.print("Nova população: ");
                    dadosTerritorio.setPopulacao(lerInteiro());
                    System.out.println("População atualizada.");
                    break;
                case 2:
                    System.out.print("Novo limite de temperatura (°C): ");
                    dadosTerritorio.setLimiteTemperatura(lerDouble());
                    System.out.println("Limite atualizado.");
                    break;
                case 3:
                    System.out.print("Novo limite de consumo de energia (kW): ");
                    dadosTerritorio.setLimiteConsumoEnergia(lerDouble());
                    System.out.println("Limite atualizado.");
                    break;
                case 4:
                    System.out.print("Novo limite de carga da rede (%): ");
                    dadosTerritorio.setLimiteCargaRede(lerDouble());
                    System.out.println("Limite atualizado.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } catch (DadosInvalidosException e) {
            System.out.println("Não foi possível alterar: " + e.getMessage());
        }
    }

    private static int lerInteiro() {
        while (true) {
            String texto = scanner.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.print("\"" + texto + "\" não é um número inteiro. Digite novamente: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            String texto = scanner.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.print("\"" + texto + "\" não é um número. Digite novamente: ");
            }
        }
    }
}