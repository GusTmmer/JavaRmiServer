package HelloWorld;

import Consultas.ConsultaHospedagem;
import Consultas.Date;
import Supervisionados.Hospedagem;
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
    
    private HotelSupplier server;
    private Scanner scanner;
    
    
    CommandParser(HotelSupplier server, Scanner scanner) {
        this.server = server;
        this.scanner = scanner;
    }

    /** Parses commands inputted by the user, getting appropriate handler.
     *
     * @param command : A string containing the command inputted by the user.
     */
    public void parseCommand(String command) {
        
        if (command.equalsIgnoreCase("add h")) {
            novaHospedagem();
        } else if (command.equalsIgnoreCase("show h")) {
            mostraHospedagens();            
        } else { 
            System.out.println("Not supported.");
        }
    }
    
    /**
     * Creates a new lodging and adds it to the available lodgings list in the server.
     * Asks for further information from the user to describe the lodging.
     */
    private void novaHospedagem() {
        
        System.out.print("Local: ");
        String location = scanner.nextLine();

        System.out.print("Primeiro dia disponivel (DD/MM/AAAA): ");
        String startDate = scanner.nextLine();
        
        System.out.print("Numero de dias disponiveis: ");
        int daysAvailable = Integer.parseInt(scanner.nextLine());

        System.out.print("Número de quartos: ");
        int spots = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Custo da diaria: ");
        String price = scanner.nextLine();

        Date firstDate = new Date(startDate);
        
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
}
