package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Board {

    private int id;
    private String nome;
    private List <Coluna> colunas = new ArrayList<>();

    public Board(String nome) {
        this.nome = nome;
        this.colunas = new ArrayList<>();
    }

    public void adicionarColuna(Coluna coluna) {
        this.colunas.add(coluna);
        this.colunas.sort(Comparator.comparing(Coluna::getOrdem));
    }

    public boolean validarEstrutura() {
        int numColunas = colunas.size();
        if (numColunas < 3) return false;

        if (colunas.get(0).getTipo() != TipoColuna.INICIAL || colunas.get(0).getOrdem() != 1) return false;
        if (colunas.get(numColunas - 2).getTipo() != TipoColuna.FINAL) return false;
        if (colunas.get(numColunas - 1).getTipo() != TipoColuna.CANCELAMENTO) return false;

        long contagemColunaInicial = colunas.stream().filter(c -> c.getTipo() == TipoColuna.INICIAL).count();
        long contagemColunaFinal = colunas.stream().filter(c -> c.getTipo() == TipoColuna.FINAL).count();
        long contagemColunaCancelamento = colunas.stream().filter(c -> c.getTipo() == TipoColuna.CANCELAMENTO).count();

        if (contagemColunaInicial != 1 || contagemColunaFinal != 1 || contagemColunaCancelamento != 1 ) return false;

        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List <Coluna> getColunas() {
        return colunas;
    }

    public void setColunas(List <Coluna> colunas) {
        this.colunas = colunas;
        this.colunas.sort(Comparator.comparing(Coluna::getOrdem));
    }
}
