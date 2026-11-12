package dominio;

import java.util.Random;

import excecao.FonteIndisponivelException;

public class SensorTemperatura implements Sensor {

    private Random random = new Random();
    private boolean funcionando = true;

    @Override
    public double lerValor() throws FonteIndisponivelException {
        // 5% de chance do sensor nao responder
        if (random.nextInt(100) < 5) {
            funcionando = false;
            throw new FonteIndisponivelException("O sensor de temperatura não respondeu.");
        }
        funcionando = true;

        // 5% de chance de uma leitura com defeito, fora da faixa
        if (random.nextInt(100) < 5) {
            return 80.0;
        }

        double valor = 22 + random.nextDouble() * 18;
        return Math.round(valor * 10) / 10.0;
    }

    @Override
    public boolean estaFuncionando() {
        return funcionando;
    }

    @Override
    public String getTipo() {
        return "TEMPERATURA";
    }

    @Override
    public String getUnidade() {
        return "°C";
    }

    @Override
    public double getValorMinimo() {
        return -10;
    }

    @Override
    public double getValorMaximo() {
        return 55;
    }
}