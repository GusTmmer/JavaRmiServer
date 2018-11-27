package HelloWorld;

import Consultas.ConsultaHospedagem;
import Consultas.ConsultaPassagem;
import Consultas.Date;
import Supervisionados.Hospedagem;
import Supervisionados.Passagem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *  Responsible for processing command inputted in the server.
 *  Adds new tickets and new lodgings.
 *  Also can show the state of the server.
 */
public class CommandParser {
    
    private CoordServer server;
    private Scanner scanner;
    
    
    CommandParser(CoordServer server, Scanner scanner) {
        this.server = server;
        this.scanner = scanner;
    }

    /** Parses commands inputted by the user, getting appropriate handler.
     *
     * @param command : A string containing the command inputted by the user.
     */
    public void parseCommand(String command) {
        
        if (command.equalsIgnoreCase("show h")) {
            mostraHospedagens();

        } else if (command.equalsIgnoreCase("show p")) {
            mostraPassagens();
            
        } else { 
            System.out.println("Not supported.");
        }
    }

    /**
     * Creates a new ticket and adds it to the available tickets list in the server.
     * Asks for further information from the user to describe the ticket.
     */


    /**
     * A query to show available lodgings on the server side.
     */
    private void mostraHospedagens() {

        System.out.print("Local: ");
        String location = scanner.nextLine();

        System.out.print("Dia de entrada (DD/MM/AAAA): ");
        String entryDate = scanner.nextLine();

        System.out.print("Dia de saída (DD/MM/AAAA): ");
        String leaveDate = scanner.nextLine();

        System.out.print("Número de quartos: ");
        int nRooms = Integer.parseInt(scanner.nextLine());

        System.out.print("Número de pessoas: ");
        int nPeople = Integer.parseInt(scanner.nextLine());

        Date entryDateObj = new Date(entryDate);
        Date leaveDateObj = new Date(leaveDate);

        ConsultaHospedagem consultaHospedagem = new ConsultaHospedagem (
                location, entryDateObj.reprDay, leaveDateObj.reprDay, nRooms, nPeople
        );

        List<Hospedagem> availableLodgings = server.consultaHospedagem(consultaHospedagem);

        if (availableLodgings == null) {
            System.out.println("Nao foram encontradas hospedagens com os critérios estabelecidos.");
            return;
        }

        for (Hospedagem h : availableLodgings) {
            System.out.printf("Preco da diaria: %s\n", h.getPrice());
        }
    }

    /**
     * A query to show available plane tickets on the server side.
     */
    private void mostraPassagens() {

        System.out.println("Passagem só de ida (y/n)?  ");
        boolean isOneWay = scanner.nextLine().startsWith("y");

        System.out.print("Origem: ");
        String origin = scanner.nextLine();

        System.out.print("Destino: ");
        String destination = scanner.nextLine();

        System.out.print("Data de ida (DD/MM/AAAA): ");
        String goingDate = scanner.nextLine();

        String returnDate = "";
        if (!isOneWay) {
            System.out.print("Data de retorno (DD/MM/AAAA): ");
            returnDate = scanner.nextLine();
        }

        System.out.print("Número de pessoas: ");
        String nPeople = scanner.nextLine();


        Date goingDateObj = new Date(goingDate);
        Date returnDateObj = new Date(returnDate);


        ConsultaPassagem consultaPassagem = new ConsultaPassagem(
                isOneWay,
                origin,
                destination,
                goingDateObj.reprDay,
                returnDateObj.reprDay,
                Integer.parseInt(nPeople)
        );

        Map<String, List<Passagem>> availableTickets = server.consultaPassagem(consultaPassagem);

        if (availableTickets == null) {
            System.out.println("Nao foram encontradas passagens com os critérios estabelecidos.");
            return;
        }

        System.out.println("Passagens de ida:");

        for (Passagem p : availableTickets.get("Ida")) {
            System.out.printf("Preco da passagem: %s\n", p.getPrice());
        }

        if (!consultaPassagem.isOneWay()) {

            System.out.println("Passagens de volta:");

            for (Passagem p : availableTickets.get("Volta")) {
                System.out.printf("Preco da passagem: %s\n", p.getPrice());
            }
        }
    }
}
