/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Supervisionados;

import Consultas.ConsultaHospedagem;
import Consultas.ConsultaPassagem;

/**
 *
 * @author a1729756
 */
public class Transacao {
    
    static int nTransactions;
    
    private int id;
    ConsultaHospedagem ch;
    ConsultaPassagem cp;
    Status status;
    
    
    public enum Status {
        PENDING_RESPONSE,
        PENDING_COMMIT,
        FINISHED;
    };
    
    Transacao(ConsultaHospedagem ch, ConsultaPassagem cp) {
        this.ch = ch;
        this.cp = cp;
        this.id = ++Transacao.nTransactions;
        this.status = Status.PENDING_RESPONSE;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
}
