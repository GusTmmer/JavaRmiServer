package HelloWorld;

import Consultas.ConsultaPassagem;
import Consultas.Date;
import Supervisionados.Passagem;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *  Responsible for processing command inputted in the server.
 *  Adds new tickets and new lodgings.
 *  Also can show the state of the server.
 */
public class CommandParser {
    
    private TicketSupplier server;
    private Scanner scanner;
    
    
    CommandParser(TicketSupplier server, Scanner scanner) {
        this.server = server;
        this.scanner = scanner;
    }

    /** Parses commands inputted by the user, getting appropriate handler.
     *
     * @param command : A string containing the command inputted by the user.
     */
    public void parseCommand(String command) {
            
        if (command.equalsIgnoreCase("add p")) {
            novaPassagem();

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
        String price = scanner.nextLine();

        Date dateObj = new Date(date);

        Passagem novaPassagem = new Passagem(
                origin,
                destination,
                dateObj.reprDay,
                Integer.parseInt(spots),
                price
        );

        server.adicionaPassagem(novaPassagem);

        System.out.println("Passagem criada.");
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
