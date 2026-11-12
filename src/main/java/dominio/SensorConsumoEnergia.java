package dominio;

import java.util.Random;

import excecao.FonteIndisponivelException;

public class SensorConsumoEnergia implements Sensor {

    private Random random = new Random();
    private boolean funcionando = true;

    @Override
    public double lerValor() throws FonteIndisponivelException {
        if (random.nextInt(100) < 5) {
            funcionando = false;
            throw new FonteIndisponivelException("O medidor de consumo de energia não respondeu.");
        }
        funcionando = true;

        if (random.nextInt(100) < 5) {
            return -50.0;
        }

        double valor = 1500 + random.nextDouble() * 2000;
        return Math.round(valor * 10) / 10.0;
    }

    @Override
    public boolean estaFuncionando() {
        return funcionando;
    }

    @Override
    public String getTipo() {
        return "CONSUMO_ENERGIA";
    }

    @Override
    public String getUnidade() {
        return "kW";
    }

    @Override
    public double getValorMinimo() {
        return 0;
    }

    @Override
    public double getValorMaximo() {
        return 5000;
    }
}