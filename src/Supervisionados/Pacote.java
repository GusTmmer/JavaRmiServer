package Supervisionados;

/**
 *
 * A class used to represent a travel package in the server.
 */
public class Pacote {
    
    public Hospedagem hospedagem;
    public Passagem passagem;

    Pacote(Hospedagem hospedagem, Passagem passagem) {
        this.hospedagem = hospedagem;
        this.passagem = passagem;
    }
}
