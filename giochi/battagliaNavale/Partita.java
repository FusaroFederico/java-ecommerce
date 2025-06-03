package org.java.snacks.ecommerce.giochi.battagliaNavale;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

import org.java.snacks.ecommerce.modelli.Utente;

public class Partita implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final Utente giocatore;
    private boolean turnoGiocatore = true;
    private boolean partitaFinita = false;
    private boolean partitaInterrotta = false;
    private Griglia grigliaGiocatore;
    private Griglia grigliaComputer;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private LocalDateTime dataInterruzione;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // serve per formattare la data 

    // costruttore
    public Partita(Utente utente, Scanner scanner) {
        this.giocatore = utente;
        this.grigliaGiocatore = new Griglia();
        this.grigliaComputer = new Griglia();
        this.dataInizio = LocalDateTime.now();
    }

    // metodo per gestire turno giocatore e turno computer
    public void gioca(Scanner scanner) {
        System.out.println("Benvenuto nella Battaglia Navale!");
        System.out.println("----------------------------------");

        while (!partitaFinita) {    // finchè la partita non è finita
        	// visualizza griglia giocatore e griglia pc
            mostraGriglie();

            // se è il turno del giocatore
            if (turnoGiocatore) {
                System.out.println("\nTURNO DI " + giocatore.getUsername());
                // salva l'output del metodo giocatoreColpisce nell'array attacco
                int[] attacco = giocatoreColpisce(scanner);
                // usa attacco come input del metodo riceviColpo della grigliaComputer
                grigliaComputer.riceviColpo(attacco[0], attacco[1]);

                // se tutte le navi del computer sono affondate
                if (grigliaComputer.tutteLeNaviAffondate()) {
                    System.out.println("Complimenti! Hai vinto!");   // visualizza il messaggio
                    dataFine = LocalDateTime.now();                  // imposta la data di fine partita
                    partitaFinita = true;                            // imposta partitaFinita su true
                    break;  // esce dal ciclo
                } else {
                	// altrimenti passa al turno del computer
                    turnoGiocatore = false;
                }
                // alla fine del turno giocatore chiede se vuoi interrompere la partita
                while (true) {
    	            // logica per interrompere la partita
    	            System.out.println("\nVuoi interrompere qui la partita? (si/no)");
    	            String scelta = scanner.nextLine();
    	            
    	            if (scelta.equalsIgnoreCase("si")) {
    	            	dataInterruzione = LocalDateTime.now();   // imposta la data di interruzione
    	            	partitaInterrotta = true;                 
    	            	return;
    	            } else if(scelta.equalsIgnoreCase("no")){
    	            	break;
    	            } else {
    	            	System.out.println("Opzione non valida. Riprova.");
    	            }
                }

            } else {
                System.out.println("\nTURNO DEL COMPUTER");
                int[] attacco = computerColpisce();            
                grigliaGiocatore.riceviColpo(attacco[0], attacco[1]);

                if (grigliaGiocatore.tutteLeNaviAffondate()) {
                    System.out.println("Peccato... hai perso!");
                    dataFine = LocalDateTime.now();
                    partitaFinita = true;
                } else {
                    turnoGiocatore = true;
                }
            }
            
            
        }

        System.out.println("\n=== FINE PARTITA ===");
        mostraGriglie(); // stampa finale
    }
    
    public static int[] giocatoreColpisce(Scanner scanner) {
        int row = -1;
        int col = -1;
        while (true) {
            try {
            	// chiede all'utente di inserire riga e colonna dell'attacco 
                System.out.print("Inserisci la riga (0-4): ");
                row = Integer.parseInt(scanner.nextLine());
                System.out.print("Inserisci la colonna (0-4): ");
                col = Integer.parseInt(scanner.nextLine());
                // se l'input è valido interrompe il ciclo
                if (row >= 0 && row < 5 && col >= 0 && col < 5) {
                    break;
                } else {
                	// altrimenti visualizza un messaggio e ripete il ciclo
                    System.out.println("Coordinate non valide, riprova.");
                }
            } catch (NumberFormatException e) {
            	// se c'è un errore, visualizza un messaggio e ripete il ciclo
                System.out.println("Input non valido, riprova.");
            }
        }
        // restituisce un array con le coordinate del colpo
        return new int[]{row, col};
    }
    
    // metodo che restituisce le coordinate dell'attacco del computer in modo casuale
    public static int[] computerColpisce() {
    	Random random = new Random();
        int row = random.nextInt(5);
        int col = random.nextInt(5);
        System.out.println("Il computer spara in (" + row + ", " + col + ")");
        return new int[]{row, col};
    }

    // metodo che permette di visualizzare le griglie
    private void mostraGriglie() {
        System.out.println("\n--- LA TUA GRIGLIA ---");
        grigliaGiocatore.mostraGriglia(true);          

        System.out.println("\n--- GRIGLIA DEL COMPUTER ---");
        grigliaComputer.mostraGriglia(false);
    }


    // metodo che restituisce il nome del vincitore
    public String getVincitore() {
        if (grigliaComputer.tutteLeNaviAffondate()) return giocatore.getUsername();
        if (grigliaGiocatore.tutteLeNaviAffondate()) return "Computer";
        return "Nessuno";
    }

    // metodo toString per visualizzare le info della partita
    @Override
    public String toString() {
    	long minuti = getDurataPartita().toMinutes();
    	long secondi = getDurataPartita().minusMinutes(minuti).getSeconds();
    	
    	return String.format("Partita iniziata il: %s | Terminata il: %s | Durata: %d min %d sec | Vincitore: %s", dataInizio.format(formatter), 
    							dataFine.format(formatter), minuti, secondi, getVincitore());
    }
    
    // getter e setter
    public boolean partitaFinita() {
    	return partitaFinita;
    }
    
    public Utente getGiocatore() {
        return giocatore;
    }

    public boolean turnoGiocatore() {
        return turnoGiocatore;
    }

    public void setTurnoGiocatore(boolean turnoGiocatore) {
        this.turnoGiocatore = turnoGiocatore;
    }
    
    public LocalDateTime getDataInterruzione() {
		return dataInterruzione;
	}

	public void setDataInterruzione(LocalDateTime dataInterruzione) {
		this.dataInterruzione = dataInterruzione;
	}

	public Duration getDurataPartita() {
    	if ( dataInizio != null && dataFine != null) {
    		return Duration.between(dataInizio, dataFine);
    	}
    	return Duration.ZERO;
    }
    
    
}
