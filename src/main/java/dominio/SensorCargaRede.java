package dominio;

import java.util.Random;

import excecao.FonteIndisponivelException;

public class SensorCargaRede implements Sensor {

    private Random random = new Random();
    private boolean funcionando = true;

    @Override
    public double lerValor() throws FonteIndisponivelException {
        if (random.nextInt(100) < 5) {
            funcionando = false;
            throw new FonteIndisponivelException("O sensor de carga da rede não respondeu.");
        }
        funcionando = true;

        if (random.nextInt(100) < 5) {
            return 200.0;
        }

        double valor = 50 + random.nextDouble() * 50;
        return Math.round(valor * 10) / 10.0;
    }

    @Override
    public boolean estaFuncionando() {
        return funcionando;
    }

    @Override
    public String getTipo() {
        return "CARGA_REDE";
    }

    @Override
    public String getUnidade() {
        return "%";
    }

    @Override
    public double getValorMinimo() {
        return 0;
    }

    @Override
    public double getValorMaximo() {
        return 150;
    }
}