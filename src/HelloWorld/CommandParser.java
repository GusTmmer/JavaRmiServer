/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import Consultas.ConsultaHospedagem;
import Consultas.Date;
import Supervisionados.Hospedagem;
import Supervisionados.Passagem;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author a1236776
 */
public class CommandParser {
    
    private ServImpl server;
    private Scanner scanner;
    
    
    CommandParser(ServImpl server, Scanner scanner) {
        this.server = server;
        this.scanner = scanner;
    }
    
    
    public void parseCommand(String command) {
        
        if (command.equalsIgnoreCase("add h")) {
            novaHospedagem();
            
        } else if (command.equalsIgnoreCase("add p")) {
            novaPassagem();
            
        //}
        //} else if (command.equalsIgnoreCase("check p")) {
            //mostra_passagens();
            
        } else { 
            System.out.println("Not supported.");
        }
    }
    
    private void novaHospedagem() {
        
        System.out.print("Local: ");
        String location = scanner.nextLine();

        System.out.print("Primeiro dia disponivel (DD/MM/AAAA): ");
        String date_start = scanner.nextLine();
        
        System.out.print("Numero de dias disponiveis: ");
        int daysAvailable = scanner.nextInt();

        System.out.print("Número de quartos: ");
        int spots = scanner.nextInt();    
        
        System.out.print("Custo da diaria: ");
        float price = scanner.nextFloat();   
        
        int day = Integer.parseInt(date_start.split("/")[0]);
        int month = Integer.parseInt(date_start.split("/")[1]);
        int year = Integer.parseInt(date_start.split("/")[2]);
        
        Date firstDate = new Date(day, month, year);
        
        Map<Integer, Integer> availableDates = new HashMap<>();

        int date = firstDate.reprDay;

        for (int i = 0; i < daysAvailable; i++)
            availableDates.put(date++, spots);

        Hospedagem novaHospedagem = new Hospedagem(
                location,
                availableDates,
                price
        );

        server.adicionaHospedagem(novaHospedagem);
        
        System.out.println("Hospedagem criada.");
    }
    
    private void novaPassagem() {
        
        System.out.print("Origem: ");
        String origin = scanner.nextLine();

        System.out.print("Destino: ");
        String destination = scanner.nextLine();

        System.out.print("Data (DD/MM/AAAA): ");
        String date = scanner.nextLine();

        System.out.print("Número de vagas: ");
        String spots = scanner.nextLine();
        
        System.out.print("Custo da passagem: ");
        float price = scanner.nextFloat();

        Date dateObj = new Date(date);

        Passagem nova_passagem = new Passagem(
                origin,
                destination,
                dateObj.reprDay,
                Integer.parseInt(spots),
                price
        );

        server.adicionaPassagem(nova_passagem);
    }
}
