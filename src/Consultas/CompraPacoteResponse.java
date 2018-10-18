package Consultas;

import Supervisionados.Hospedagem;
import Supervisionados.Passagem;

import java.util.Map;

/**
 * A specialized class used by the server to reply to a client that queried a travel package request.
 * Different from the similar "ConsultaPacoteResponse", this class can only hold at maximum two tickets and
 * a single lodging.
 */
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
