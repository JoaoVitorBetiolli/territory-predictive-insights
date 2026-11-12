package dominio;

import excecao.FonteIndisponivelException;

public interface Sensor {

    double lerValor() throws FonteIndisponivelException;

    boolean estaFuncionando();

    String getTipo();

    String getUnidade();

    double getValorMinimo();

    double getValorMaximo();
}