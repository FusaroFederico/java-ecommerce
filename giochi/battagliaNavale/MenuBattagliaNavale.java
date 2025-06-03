package org.java.snacks.ecommerce.giochi.battagliaNavale;

import java.util.List;
import java.util.Scanner;

import org.java.snacks.ecommerce.modelli.Utente;

public class MenuBattagliaNavale {
	
	public static void menuPrincipale(Scanner scanner, Utente utenteLoggato, GestioneSalvataggi gestioneSalvataggi) {
		// cerca e carica il salvataggio dell'utente
		Salvataggio salvataggio = gestioneSalvataggi.getSalvataggioUtente(utenteLoggato);
		// se non lo trova ne crea uno nuovo e lo aggiunge ai salvataggi
		if (salvataggio == null) {
			salvataggio = new Salvataggio(utenteLoggato);
			gestioneSalvataggi.aggiungiSalvataggio(salvataggio);
		}
		
		while (true) {
            System.out.println("\n=== BATTAGLIA NAVALE ===");
            System.out.println("1. Nuova partita");
            System.out.println("2. Continua partita salvata");
            System.out.println("3. Mostra storico partite concluse");
            //System.out.println("4. Mostra tutti i salvataggi");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    nuovaPartita(scanner, utenteLoggato, salvataggio, gestioneSalvataggi);
                    break;
                case "2":
                    continuaPartita(scanner, salvataggio, gestioneSalvataggi);
                    break;
                case "3":
                    mostraStorico(salvataggio);
                    break;
                /**case "4":
                	gestioneSalvataggi.mostraTuttiSalvataggi();
                	break;*/
                case "0":
                    gestioneSalvataggi.salva();
                    System.out.println("Salvataggio effettuato. Uscita...");
                    return;
                default:
                    System.out.println("Scelta non valida, riprova.");
            }
        }
	}
	
	// metodo per gestire le nuove partite
	private static void nuovaPartita(Scanner scanner, Utente utenteLoggato, Salvataggio salvataggio, GestioneSalvataggi gestioneSalvataggi) {
       
        Partita partita = new Partita(utenteLoggato, scanner); // crea una nuova partita
        partita.gioca(scanner);         // gioca

        if (partita.partitaFinita()) {
        	// se la partita è finita, la aggiunge alle partite finite
            salvataggio.aggiungiPartitaTerminata(partita);
        } else {
        	// se la partita non è finita la aggiunge a quelle in corso
            salvataggio.aggiungiPartitaInCorso(partita);
            System.out.println("Partita salvata, puoi riprenderla quando vuoi.");
        }
        // salva
        gestioneSalvataggi.salva();
    }
	
	// metodo per continuare una partita già iniziata
	private static void continuaPartita(Scanner scanner, Salvataggio salvataggio, GestioneSalvataggi gestioneSalvataggi) {
        // carica la lista delle partite in corso
		List<Partita> partite = salvataggio.getPartiteInCorso();

		// se è vuota, stampa un messaggio e esce
        if (partite.isEmpty()) {
            System.out.println("Nessuna partita salvata da continuare.");
            return;
        }

        // visualizza la lista delle partite
        System.out.println("Partite disponibili:");
        for (int i = 0; i < partite.size(); i++) {
            Partita partita = partite.get(i);
            System.out.printf("%d. Partita interrota il: %s", i + 1, partita.getDataInterruzione().format(Partita.formatter));
        }

        // inizializza la variabile scelta a -1 e finchè l'utente non inserisce un numero di partita valido
        // o 0 per uscire, continua nel loop del while
        int scelta = -1;
        while (scelta < 0 || scelta > partite.size()) {
            System.out.println("\nScegli il numero della partita da riprendere (0 per uscire): ");
            try {
                scelta = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ignored) {}
        }
        
        if(scelta == 0) {
        	return;
        }
        
        Partita partita = partite.remove(scelta - 1); // rimuove temporaneamente la partita salvata
        // da qui fa esattamente le stesse cose di prima
        partita.gioca(scanner);

        if (partita.partitaFinita()) {
            salvataggio.aggiungiPartitaTerminata(partita);
        } else {
            salvataggio.aggiungiPartitaInCorso(partita);
            System.out.println("Partita salvata, puoi riprenderla quando vuoi.");
        }
        gestioneSalvataggi.salva();
    }

	// metodo per visualizzare a schermo le partite terminate
    private static void mostraStorico(Salvataggio salvataggio) {
        if (salvataggio.getStoricoPartite().isEmpty()) {
            System.out.println("Nessuna partita completata ancora.");
        } else {
            System.out.println("\n--- Ultime 3 partite concluse ---");
            for (String riga : salvataggio.getStoricoPartite()) {
                System.out.println("- " + riga);
            }
        }
    }
	
}
