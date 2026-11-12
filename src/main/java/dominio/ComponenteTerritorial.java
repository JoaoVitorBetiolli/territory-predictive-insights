package dominio;

public abstract class ComponenteTerritorial {

    private int id;
    private String nome;
    private String regiao;
    private String status;

    public ComponenteTerritorial(int id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.status = "NÃO VERIFICADO";
    }

    public abstract void verificarStatus();

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRegiao() {
        return regiao;
    }

    public String getStatus() {
        return status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }
}