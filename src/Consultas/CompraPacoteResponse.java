package Consultas;

import Supervisionados.Hospedagem;
import Supervisionados.Passagem;

import java.util.Map;

public class CompraPacoteResponse {

    private Hospedagem hospedagem;
    private Map<String, Passagem> passagens;

    public CompraPacoteResponse(Hospedagem hospedagem, Map<String, Passagem> passagens) {
        this.hospedagem = hospedagem;
        this.passagens = passagens;
    }

    public Hospedagem getHospedagem() {
        return hospedagem;
    }

    public Map<String, Passagem> getPassagens() {
        return passagens;
    }

}
