package org.java.snacks.ecommerce;
import java.io.*;
import java.util.*;

import org.java.snacks.ecommerce.gestione.GestoreProdotti;
import org.java.snacks.ecommerce.gestione.GestoreUtenti;
import org.java.snacks.ecommerce.modelli.Prodotto;
import org.java.snacks.ecommerce.modelli.Utente;

public class AppStarter {
	private static final String FILE_UTENTI = "utenti.dat";
    private static final String FILE_PRODOTTI = "prodotti.dat";
    private static GestoreUtenti gestoreUtenti;
    private static GestoreProdotti gestoreProdotti;
    private static Scanner scanner;
    private static Utente utenteLoggato;

	public static void main(String[] args) {
		inizializzazione();
        mostraMenuIniziale();
	}
	
	private static void inizializzazione() {
        scanner = new Scanner(System.in);
        gestoreUtenti = new GestoreUtenti(FILE_UTENTI);
        gestoreProdotti = new GestoreProdotti(FILE_PRODOTTI);
        
        // Caricamento dati da file
        gestoreUtenti.caricaUtenti();
        // Aggiorna dati utenti
        // Ogni volta che fai una modifica ad una classe modello
        // ricordati di aggiornare i dati con le cose mancanti o
        // potrebbero avvenire delle eccezioni impreviste
        /** aggiornamento dopo aver implementato il carrello
         * for ( Utente utente : gestoreUtenti.getUtentiRegistrati()) {
        	if (utente.getCarrello() == null) {
        		utente.setCarrello(new ArrayList<>());
        	}
        }**/
        // aggiornamento dopo aver implementato il saldo
        /**for (Utente utente : gestoreUtenti.getUtentiRegistrati()) {
        	utente.setSaldo(0.00);
        }
        gestoreUtenti.salvaUtenti();**/
        gestoreProdotti.caricaProdotti();
    }
	
	private static void mostraMenuIniziale() {
        while (true) {
            System.out.println("\n===== NEGOZIO ONLINE =====");
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("0. Esci");
            System.out.print("Scegli un'opzione dal menù: ");
            
            int sceltaUtente = getIntInput();
            
            switch (sceltaUtente) {
                case 1:
                    login();
                    break;
                case 2:
                    registrazione();
                    break;
                case 0:
                    System.out.println("Grazie per aver utilizzato il nostro store. Arrivederci!");
                    System.exit(0);
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }
	
	private static void login() {
        System.out.println("\n===== LOGIN =====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        Utente utente = gestoreUtenti.autenticazione(username, password);
        if (utente != null) {
            utenteLoggato = utente;
            System.out.println("Login effettuato con successo. Benvenuto " + utente.getUsername() + "!");
            mostraMenuPrincipale();
        } else {
            System.out.println("Username o password non validi.");
        }
    }
    
    private static void registrazione() {
        System.out.println("\n===== REGISTRAZIONE =====");
        System.out.print("Inserisci il tuo Username: ");
        String username = scanner.nextLine();
        
        // Verifica se l'username esiste già
        if (gestoreUtenti.esisteUtente(username)) {
            System.out.println("Username già esistente. Scegli un altro username.");
            return;
        }
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        Utente nuovoUtente = new Utente(username, password);
        gestoreUtenti.aggiungiUtente(nuovoUtente);
        gestoreUtenti.salvaUtenti();
        
        System.out.println("Registrazione completata con successo! Ora puoi effettuare il login.");
    }
    
    private static void mostraMenuPrincipale() {
        while (true) {
            System.out.println("\n===== MENU PRINCIPALE =====");
            System.out.println("1. Visualizza tutti i prodotti");
            System.out.println("2. Cerca prodotto");
            System.out.println("3. Acquista prodotto");
            System.out.println("4. Visualizza carrello");
            System.out.println("5. Metti in vendita un prodotto");
            System.out.println("6. I miei prodotti in vendita");
            System.out.println("7. Il mio profilo");
            System.out.println("0. Logout");
            System.out.print("Scegli un'opzione: ");
            
            int sceltaUtente = getIntInput();
            
            switch (sceltaUtente) {
                case 1:
                    mostraTuttiProdotti();
                    break;
                case 2:
                    cercaProdotto();
                    break;
                case 3:
                    acquistaProdotto();
                    break;
                case 4:
                	mostraCarrello();
                    break;
                case 5:
                	vendiProdotto();
                    break;
                case 6:
                	mostraMieiProdotti();
                    break;
                case 7:
                	mostraProfilo();
                	break;
                case 0:
                    utenteLoggato = null;
                    System.out.println("Logout effettuato con successo.");
                    return;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }
    
    private static void mostraTuttiProdotti() {
        System.out.println("\n===== TUTTI I PRODOTTI =====");
        List<Prodotto> prodotti = gestoreProdotti.prendiTuttiProdotti();
        
        if (prodotti.isEmpty()) {
            System.out.println("Nessun prodotto disponibile al momento.");
            return;
        }
        
        for (int i = 0; i < prodotti.size(); i++) {
            Prodotto prodotto = prodotti.get(i);
            System.out.println((i+1) + ". " + prodotto);
        }
        
        System.out.println("\nVuoi aggiungere un prodotto al carrello? (si/no): ");
        String scelta = scanner.nextLine().trim().toLowerCase();
        
        if (scelta.equals("si")) {
        	System.out.println("Inserisci il numero del prodotto da aggiungere al carrello: ");
        	int numeroProdotto = getIntInput();
        	
        	if (numeroProdotto <= 0 || numeroProdotto > prodotti.size() ) {
        		System.out.println("Numero prodotto non valido.");
        		return;
        	}
        	
        	if (!utenteLoggato.getCarrello().contains(prodotti.get(numeroProdotto - 1))) {
        		utenteLoggato.aggiungiAlCarrello(prodotti.get(numeroProdotto - 1));
        		gestoreUtenti.salvaUtenti();
        		System.out.println("Prodotto aggiunto correttamente!");
        	} else {
        		System.out.println("Il prodotto è già presente nel carrello.");
        	}
        }
        
    }
    
    private static void cercaProdotto() {
        System.out.println("\n===== CERCA PRODOTTO =====");
        System.out.print("Inserisci il nome del prodotto da cercare: ");
        String parolaChiave = scanner.nextLine();
        
        List<Prodotto> risultati = gestoreProdotti.cercaProdotti(parolaChiave);
        
        if (risultati.isEmpty()) {
            System.out.println("Nessun prodotto trovato con '" + parolaChiave + "'.");
            return;
        }
        
        System.out.println("\nRisultati della ricerca:");
        for (int i = 0; i < risultati.size(); i++) {
            Prodotto prodotto = risultati.get(i);
            System.out.println((i+1) + ". " + prodotto);
        }
    }
    
    private static void acquistaProdotto() {
        System.out.println("\n===== ACQUISTA PRODOTTO =====");
        List<Prodotto> prodottiDisponibili = gestoreProdotti.prendiTuttiProdotti();
        
        // rimuove i prodotti esauriti
        for(Prodotto prodotto : prodottiDisponibili) {
        	if (prodotto.getQuantita() <= 0) {
        		prodottiDisponibili.remove(prodotto);
        	}
        }
        
        if (prodottiDisponibili.isEmpty()) {
            System.out.println("Nessun prodotto disponibile per l'acquisto.");
            return;
        }
        
        System.out.println("Prodotti disponibili:");
        for (int i = 0; i < prodottiDisponibili.size(); i++) {
            Prodotto prodotto = prodottiDisponibili.get(i);
            System.out.println((i+1) + ". " + prodotto);
        }
        
        System.out.print("\nInserisci il numero del prodotto che vuoi acquistare (0 per annullare): ");
        int sceltaUtente = getIntInput();
        
        if (sceltaUtente == 0) {
            System.out.println("Acquisto annullato.");
            return;
        }
        
        if (sceltaUtente < 1 || sceltaUtente > prodottiDisponibili.size()) {
            System.out.println("Scelta non valida.");
            return;
        }
        
        Prodotto prodottoScelto = prodottiDisponibili.get(sceltaUtente - 1);
        
        // Non permettere l'acquisto dei propri prodotti
        if (prodottoScelto.getIdVenditore().equals(utenteLoggato.getUsername())) {
            System.out.println("Non puoi acquistare i tuoi stessi prodotti.");
            return;
        }
        
        System.out.println("Hai selezionato: " + prodottoScelto);
        System.out.print("Confermi l'acquisto? (si/no): ");
        String conferma = scanner.nextLine().trim().toLowerCase();
        
        if (conferma.equals("si")) {
        	
        	if (prodottoScelto.getPrezzo() > utenteLoggato.getSaldo()) {
        		System.out.println("Il tuo saldo non è sufficiente per acquistare questo prodotto!");
        		return;
        	}
        	// Aggiorna saldo compratore e venditore
        	utenteLoggato.effettuaPagamento(prodottoScelto.getPrezzo());
        	for(Utente utente : gestoreUtenti.getUtentiRegistrati()) {
        		if(utente.getUsername().equals(prodottoScelto.getIdVenditore())) {
        			utente.effettuaRicarica(prodottoScelto.getPrezzo());
        		}
        	}
        	gestoreUtenti.salvaUtenti();
            // Aggiorna lo stato del prodotto
            prodottoScelto.decrementaQuantita();
            gestoreProdotti.salvaProdotti();
            
            System.out.println("Acquisto completato con successo!");
        } else {
            System.out.println("Acquisto annullato.");
        }
    }
    
    private static void vendiProdotto() {
        System.out.println("\n===== METTI IN VENDITA UN PRODOTTO =====");
        System.out.print("Nome del prodotto: ");
        String nome = scanner.nextLine();
        System.out.print("Descrizione: ");
        String descrizione = scanner.nextLine();
        System.out.print("Prezzo (€): ");
        double prezzo = getDoubleInput();
        System.out.print("Quantità disponibile: ");
        int quantita = getIntInput();
        
        if (quantita <= 0) {
            System.out.println("La quantità deve essere maggiore di zero.");
            return;
        }
        
        Prodotto nuovoProdotto = new Prodotto(
            gestoreProdotti.prossimoIdProdotto(),
            nome,
            descrizione,
            prezzo,
            quantita,
            utenteLoggato.getUsername()
        );
        
        gestoreProdotti.aggiungiProdotto(nuovoProdotto);
        gestoreProdotti.salvaProdotti();
        
        System.out.println("Prodotto aggiunto con successo!");
    }
    
    private static void mostraMieiProdotti() {
        System.out.println("\n===== I MIEI PRODOTTI IN VENDITA =====");
        List<Prodotto> mieiProdotti = gestoreProdotti.cercaProdottiDatoVenditore(utenteLoggato.getUsername());
        
        if (mieiProdotti.isEmpty()) {
            System.out.println("Non hai prodotti in vendita.");
            return;
        }
        
        for (int i = 0; i < mieiProdotti.size(); i++) {
            Prodotto prodotto = mieiProdotti.get(i);
            System.out.println((i+1) + ". " + prodotto + " ");
        }
    }
    
    private static void mostraProfilo() {
        System.out.println("\n===== PROFILO UTENTE =====");
        System.out.println("Username: " + utenteLoggato.getUsername());
    }
    
    // metodi per la gestione del carrello (sarebbe meglio creare una classe apposita)
    public static void mostraCarrello() {
    	 System.out.println("\n===== CARRELLO =====");
    	List<Prodotto> carrello = utenteLoggato.getCarrello();
    	
    	if(carrello.isEmpty()) {
    		System.out.println("Il tuo carrello è vuoto.");
    		return;
    	}
    	
    	System.out.println("Al momento nel tuo carrello ci sono i seguenti prodotti:");
    	for (int i = 0; i < carrello.size(); i++) {
            Prodotto prodotto = carrello.get(i);
            System.out.println((i+1) + ". " + prodotto + " ");
        }
    	
    	// Opzioni carrello
    	System.out.println("\nOpzioni carrello:");
    	System.out.println("1. Rimuovi un prodotto");
    	System.out.println("2. Acquista un prodotto");
    	System.out.println("Scegli un'opzione (0 per uscire dal menù carrello): ");
    	int scelta = getIntInput();
    	switch (scelta) {
    	case 0:
    		return;
    	case 1:
    		System.out.println("Inserisce il numero del prodotto che vuoi rimuovere: ");
    		int numeroProdottoRim = getIntInput();
    		if ( numeroProdottoRim > 0 && numeroProdottoRim <= carrello.size()) {
    			rimuoviProdottoDalCarrello(numeroProdottoRim - 1);
    		} else {
    			System.out.println("Numero prodotto non valido.");
    		}
    		break;
    	case 2:
    		System.out.println("Inserisce il numero del prodotto che vuoi acquistare: ");
    		int numeroProdottoAcq = getIntInput();
    		if ( numeroProdottoAcq > 0 && numeroProdottoAcq <= carrello.size() ) {
    			acquistaProdottoDalCarrello(numeroProdottoAcq - 1);
    		} else {
    			System.out.println("Numero prodotto non valido.");
    		}
    		break;
    	default:
    		System.out.println("Opzione non valida.");
    	}
    }
    
    public static void rimuoviProdottoDalCarrello(int numeroProdotto) {
    	Prodotto prodottoDaRimuovere = utenteLoggato.getCarrello().get(numeroProdotto);
    	System.out.println("Sei sicuro di voler rimuovere " + prodottoDaRimuovere.getNome() + " dal carrello? (si/no): ");
    	String scelta = scanner.nextLine().trim().toLowerCase();
    	
    	if (scelta.equals("si")) {
    		utenteLoggato.rimuoviDalCarrello(prodottoDaRimuovere);
    		gestoreUtenti.salvaUtenti();
    		System.out.println("Il prodotto è stato rimosso correttamente.");
    	}
    }
    
    public static void acquistaProdottoDalCarrello(int numeroProdotto) {
    	Prodotto prodottoSelezionato = utenteLoggato.getCarrello().get(numeroProdotto);
    	
    	// Non permettere l'acquisto dei propri prodotti
        if (prodottoSelezionato.getIdVenditore().equals(utenteLoggato.getUsername())) {
            System.out.println("Non puoi acquistare i tuoi stessi prodotti.");
            return;
        }
        // Non permette l'acquisto di prodotti esauriti
        if (prodottoSelezionato.getQuantita() == 0) {
        	System.out.println("Spiacenti, il prodotto è esaurito.");
            return;
        }
        
    	System.out.println("Sei sicuro di voler acquistare " + prodottoSelezionato.getNome() + " ? (si/no): ");
    	String conferma = scanner.nextLine().trim().toLowerCase();
    	
    	if (conferma.equals("si")) {
    		if (prodottoSelezionato.getPrezzo() > utenteLoggato.getSaldo()) {
        		System.out.println("Il tuo saldo non è sufficiente per acquistare questo prodotto!");
        		return;
        	}
        	// Aggiorna saldo compratore e venditore
        	utenteLoggato.effettuaPagamento(prodottoSelezionato.getPrezzo());
        	for(Utente utente : gestoreUtenti.getUtentiRegistrati()) {
        		if(utente.getUsername().equals(prodottoSelezionato.getIdVenditore())) {
        			utente.effettuaRicarica(prodottoSelezionato.getPrezzo());
        		}
        	}
        	gestoreUtenti.salvaUtenti();
    		prodottoSelezionato.decrementaQuantita();
    		utenteLoggato.rimuoviDalCarrello(prodottoSelezionato);
    		gestoreProdotti.salvaProdotti();
    		gestoreUtenti.salvaUtenti();
    		System.out.println("Acquisto avvenuto con successo.");
    	}
    }
    
	// 2 metodi per prendere gli input dell'utente e gestire le eccezione in caso di input errato
	private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
