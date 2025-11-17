package model;

public enum TipoColuna {

    INICIAL("Inicial"),
    PENDENTE("Pendente"),
    FINAL("Concluido"),
    CANCELAMENTO("Cancelado");

    private final String descricao;

    TipoColuna(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
