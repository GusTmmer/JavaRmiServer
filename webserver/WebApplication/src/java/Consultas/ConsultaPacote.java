/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

/**
 *
 * @author a1729756
 */
public class ConsultaPacote {
    public ConsultaHospedagem consultaHospedagem;
    public ConsultaPassagem consultaPassagem;

    public ConsultaPacote(ConsultaHospedagem h, ConsultaPassagem p) {
        consultaHospedagem = h;
        consultaPassagem = p;
        System.out.println("Criei consulta pacote");
    }
    
    
}
