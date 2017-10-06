package com.giulia.floatingactionbutton;

/**
 * Created by giulia on 03/10/17.
 */

public class SelectedItem {
    private String nome;
    private int pos;

    public SelectedItem(String nome, int pos) {
        this.nome = nome;
        this.pos = pos;
    }
    public SelectedItem(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}

