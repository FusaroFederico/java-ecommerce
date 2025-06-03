package org.java.snacks.ecommerce.gestione;

import java.util.Scanner;

import org.java.snacks.ecommerce.giochi.battagliaNavale.GestioneSalvataggi;
import org.java.snacks.ecommerce.giochi.battagliaNavale.MenuBattagliaNavale;
import org.java.snacks.ecommerce.modelli.Utente;
import org.java.snacks.ecommerce.util.Sessione;

public class GestioneMenu {

	// metodo che mostra il menù iniziale con login, registrati o esci
	public static void mostraMenuIniziale(Scanner scanner, Sessione sessione, GestoreUtenti gestoreUtenti, GestoreProdotti gestoreProdotti,
												GestioneSalvataggi gestioneSalvataggi) {
        while (true) {
            System.out.println("\n===== NEGOZIO ONLINE =====");
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("0. Esci");
            System.out.print("Scegli un'opzione dal menù: ");
            
            int sceltaUtente = getIntInput(scanner);
            
            switch (sceltaUtente) {
                case 1:
                	// lancia il metodo Login
                    login(scanner, sessione, gestoreUtenti, gestoreProdotti, gestioneSalvataggi);
                    break;
                case 2:
                	// lancia il metodo registrazione
                    registrazione(scanner, gestoreUtenti);
                    break;
                case 0:
                	// chiude il programma
                    System.out.println("Grazie per aver utilizzato il nostro store. Arrivederci!");
                    System.exit(0);
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }
	
	// metodo per il login utente
	private static void login(Scanner scanner, Sessione sessione, GestoreUtenti gestoreUtenti, GestoreProdotti gestoreProdotti,
										GestioneSalvataggi gestioneSalvataggi) {
		// chiede all'utente di inserire i proprio username e password
        System.out.println("\n===== LOGIN =====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        // effettua l'autenticazione
        Utente utente = gestoreUtenti.autenticazione(username, password);
        // se l'autenticazione è avvenuta con successo salva l'utente come utenteLoggato,
        // e lancia il metodo mostraMenuPrincipale
        if (utente != null) {
            sessione.setUtenteLoggato(utente);
            System.out.println("Login effettuato con successo. Benvenuto " + utente.getUsername() + "!");
            mostraMenuPrincipale(scanner, sessione, gestoreUtenti, gestoreProdotti, gestioneSalvataggi);
        } else {
        	// Altrimenti visualizza un opportuno messaggio
            System.out.println("Username o password non validi.");
        }
    }
    
	// metodo per la registrazione
    private static void registrazione(Scanner scanner, GestoreUtenti gestoreUtenti) {
        System.out.println("\n===== REGISTRAZIONE =====");
        System.out.print("Inserisci il tuo Username: ");
        String username = scanner.nextLine();
        
        // Verifica se l'username esiste già
        if (gestoreUtenti.esisteUtente(username)) {
            System.out.println("Username già esistente. Scegli un altro username.");
            return;
        }
        
        // chiede di inserire una password
        // N.B. non viene fatto nessun controllo sulla password ( ti puoi divertire a implementarlo )
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        // crea una nuova istanza di utente con username e password e salva i dati
        Utente nuovoUtente = new Utente(username, password);
        gestoreUtenti.aggiungiUtente(nuovoUtente);
        gestoreUtenti.salvaUtenti();
        
        System.out.println("Registrazione completata con successo! Ora puoi effettuare il login.");
    }
    
    // metodo che mostra il menù principale con tutte le varie opzioni
    private static void mostraMenuPrincipale(Scanner scanner, Sessione sessione, GestoreUtenti gestoreUtenti, GestoreProdotti gestoreProdotti, 
    											GestioneSalvataggi gestioneSalvataggi) {
        while (true) {
            System.out.println("\n===== MENU PRINCIPALE =====");
            System.out.println("1. Visualizza tutti i prodotti");
            System.out.println("2. Cerca prodotto");
            System.out.println("3. Acquista prodotto");
            System.out.println("4. Visualizza carrello");
            System.out.println("5. Metti in vendita un prodotto");
            System.out.println("6. I miei prodotti in vendita");
            System.out.println("7. Il mio profilo");
            System.out.println("8. Giochi");
            System.out.println("0. Logout");
            System.out.println("Scegli un'opzione: ");
            
            int sceltaUtente = getIntInput(scanner);
            
            switch (sceltaUtente) {
                case 1:
                    gestoreProdotti.mostraTuttiProdotti(scanner, sessione.getUtenteLoggato(), gestoreUtenti);
                    break;
                case 2:
                	gestoreProdotti.cercaProdottoPerNome(scanner, sessione.getUtenteLoggato(), gestoreUtenti);
                    break;
                case 3:
                	gestoreProdotti.acquistaProdotto(scanner, sessione.getUtenteLoggato(), gestoreUtenti);
                    break;
                case 4:
                	GestoreCarrello.mostraCarrello(sessione.getUtenteLoggato(), gestoreUtenti, gestoreProdotti, scanner);
                    break;
                case 5:
                	gestoreProdotti.vendiProdotto(scanner, sessione.getUtenteLoggato());
                    break;
                case 6:
                	gestoreProdotti.mostraMieiProdotti(sessione.getUtenteLoggato());
                    break;
                case 7:
                	mostraProfilo(sessione.getUtenteLoggato(), scanner, gestoreUtenti);
                	break;
                case 8:
                	MenuBattagliaNavale.menuPrincipale(scanner, sessione.getUtenteLoggato(), gestioneSalvataggi);
                	break;
                case 0:
                    sessione.logout();
                    System.out.println("Logout effettuato con successo.");
                    return;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }
    
    // metodo per la visualizzazione e gestione del proprio profilo utente
    private static void mostraProfilo(Utente utenteLoggato, Scanner scanner, GestoreUtenti gestoreUtenti) {
        System.out.println("\n===== PROFILO UTENTE =====");
        System.out.println("Username: " + utenteLoggato.getUsername());
        System.out.println("Saldo: " + String.format("%.2f €", utenteLoggato.getSaldo()));
        
        System.out.println("\nOpzioni profilo:");
        System.out.println("1. Cambia username");
        System.out.println("2. Effettua una ricarica");
        System.out.println("3. Cambia password");
        System.out.println("Scegli una delle opzioni (0 per uscire dal profilo): ");
        int scelta = getIntInput(scanner);
        
        switch (scelta) {
        case 0:
        	break;
        case 1:
        	System.out.println("\n===== CAMBIA USERNAME =====");
        	System.out.println("Vuoi cambiare username? (si/no) ");
        	String conferma = scanner.nextLine().trim().toLowerCase();
        	
        	if (conferma.equals("si")) {
        		System.out.println("Inserisci il nuovo username: ");
        		String nuovoUsername = scanner.nextLine();
        		
        		// controlla se lo username è già presente nel database
        		if (gestoreUtenti.esisteUtente(nuovoUsername)) {
        			System.out.println("Questo username non è disponibile!");
    				return;
        		}
        		
        		System.out.println("Username disponibile. Inserisci la tua password per confermare la tua identità: ");
        		String password = scanner.nextLine();
        		// controlla che la password sia giusta
        		if (!password.equals(utenteLoggato.getPassword())) {
        			System.out.println("Password errata! Riprova.");
        			return;
        		}
        		// se tutto corrisponde salva i nuovi dati
        		utenteLoggato.setUsername(nuovoUsername);
        		gestoreUtenti.salvaUtenti();
        		System.out.println("Username cambiato con successo!");
        	} else {
        		System.out.println("Operazione annullata!");
        	}
        	break;
        case 2:
        	System.out.println("\n===== EFFETTUA RICARICA =====");
        	// array di double con gli importi possibili
        	double[] importiDisponibili = {20.00, 50.00, 100.00};
        	System.out.println("\nImporti disponibili:");
        	// per ogni elemento dell'array genera una riga e la visualizza
        	for (int i = 0; i < importiDisponibili.length; i++) {
        		System.out.println((i+1) + ". " + String.format("%.2f €", importiDisponibili[i]));
        	}
        	
        	System.out.println("Seleziona un importo tra quelli disponibili: ");
        	int importoScelto = getIntInput(scanner);
        	
        	if ( importoScelto <= 0 || importoScelto > importiDisponibili.length) {
        		System.out.println("Scelta non valida.");
        		break;
        	}
        
        	System.out.println("Stai per ricaricare il tuo saldo di '" + importiDisponibili[importoScelto - 1] + " €' .");
        	System.out.println("Sicuro di voler procedere? (si/no)");
        	String conferma2 = scanner.nextLine().trim().toLowerCase();
        	
        	if (conferma2.equals("si")) {
        		System.out.println("Inserisci la tua password per sicurezza:");
        		String password = scanner.nextLine();
        		
        		if(!password.equals(utenteLoggato.getPassword())) {
        			System.out.println("Password errata! Riprova.");
        		}
        		
        		utenteLoggato.effettuaRicarica(importiDisponibili[importoScelto - 1]);
        		gestoreUtenti.salvaUtenti();
        		System.out.println("Ricarica effettuata con successo!");
        	} else {
        		System.out.println("Operazione annullata!");
        	}
        	break;
        case 3:
        	System.out.println("\n===== CAMBIA PASSWORD =====");
        	
        	System.out.println("Sicuro di voler cambiare password? (si/no):");
        	String conferma3 = scanner.nextLine().trim().toLowerCase();
        	
        	if(conferma3.equals("si")) {
        		System.out.println("Inserisci la password attuale:");
        		String passwordAttuale = scanner.nextLine();
        		
        		if (!passwordAttuale.equals(utenteLoggato.getPassword())) {
        			System.out.println("Password errata!");
        			return;
        		}
        		
        		System.out.println("Password corretta!");
        		System.out.println("Inserisci la nuova password:");
        		String nuovaPassword = scanner.nextLine();
        		
        		System.out.println("Inserisci nuovamente la password per conferma:");
        		String nuovaPassword2 = scanner.nextLine();
        		
        		if (!nuovaPassword.equals(nuovaPassword2)) {
        			System.out.println("Le password non corrispondono! Riprova.");
        			return;
        		}
        		
        		utenteLoggato.setPassword(nuovaPassword2);
        		gestoreUtenti.salvaUtenti();
        		System.out.println("Password cambiata correttamente!");
        	} else {
        		System.out.println("Operazione annullata!");
        	}
        	break;
        }
    }
    
    // 2 metodi per prendere gli input dell'utente e gestire le eccezione in caso di input errato
    // li potresti mettere in una classe di utilità dove vanno tutti questi metodi condivisi
    public static int getIntInput(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public static double getDoubleInput(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
