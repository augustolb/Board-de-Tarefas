package model;

import java.util.ArrayList;
import java.util.List;

public class Coluna {

    private int id;
    private int idBoard;


    private String nome;
    private int ordem;
    private TipoColuna tipo;

    private List <Card> cards;

    public Coluna(String nome, int ordem, TipoColuna tipo) {
        this.nome = nome;
        this.ordem = ordem;
        this.tipo = tipo;
        this.cards = new ArrayList<>();
    }

    public void adicionarCard(Card card) {
        this.cards.add(card);
    }

    public void removerCard(Card card) {
        this.cards.remove(card);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoColuna getTipo() {
        return tipo;
    }

    public int getOrdem() {
        return ordem;
    }

    public String getNome() {
        return nome;
    }

    public List <Card> getCards() {
        return cards;
    }

    public void setCards(List <Card> cards) {
        this.cards = cards;
    }

    public int getIdBoard() {
        return idBoard;
    }

    public void setIdBoard(int idBoard) {
        this.idBoard = idBoard;
    }

}
