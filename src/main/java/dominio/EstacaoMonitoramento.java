package dominio;

import java.util.ArrayList;
import java.util.List;

public class EstacaoMonitoramento extends ComponenteTerritorial {

    private List<Sensor> sensores = new ArrayList<>();

    public EstacaoMonitoramento(int id, String nome, String regiao) {
        super(id, nome, regiao);
    }

    public void adicionarSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    @Override
    public void verificarStatus() {
        if (sensores.isEmpty()) {
            setStatus("SEM SENSORES");
            return;
        }

        int comFalha = 0;
        for (Sensor sensor : sensores) {
            if (!sensor.estaFuncionando()) {
                comFalha++;
            }
        }

        if (comFalha == 0) {
            setStatus("OPERANDO");
        } else {
            setStatus(comFalha + " SENSOR(ES) COM FALHA");
        }
    }
}