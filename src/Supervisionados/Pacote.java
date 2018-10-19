package Supervisionados;

import java.io.Serializable;

/**
 *
 * A class used to represent a travel package in the server.
 */
public class Pacote implements Serializable{
    
    public Hospedagem hospedagem;
    public Passagem passagem;

    Pacote(Hospedagem hospedagem, Passagem passagem) {
        this.hospedagem = hospedagem;
        this.passagem = passagem;
    }
}
