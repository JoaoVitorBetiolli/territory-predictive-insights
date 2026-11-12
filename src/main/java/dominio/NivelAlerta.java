package dominio;

public enum NivelAlerta {

    ATENCAO("ATENÇÃO"),
    ALERTA("ALERTA"),
    CRITICO("CRÍTICO");

    private String texto;

    NivelAlerta(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}