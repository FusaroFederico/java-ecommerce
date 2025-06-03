package org.java.snacks.ecommerce.gestione;
import java.io.*;
import java.util.*;

import org.java.snacks.ecommerce.modelli.Prodotto;
import org.java.snacks.ecommerce.modelli.Utente;

public class GestoreProdotti {
	// variabili di istanza
	private List<Prodotto> inventario;
    private String fileProdotti;
    
    // costruttore
    public GestoreProdotti(String nomeFile) {
        this.fileProdotti = nomeFile;
        this.inventario = new ArrayList<>();
    }

    // metodo che ci permette di caricare i dati da file
    public void caricaProdotti() {
        File file = new File(fileProdotti);
        if (!file.exists()) {
            return;
        }
        // blocco try-catch che prende i dati e li converte in una List java.
        // se avviene un eccezione visualizza un messaggio di errore
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            inventario = (ArrayList<Prodotto>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento dei prodotti: " + e.getMessage());
            // In caso di errore, inizializza una nuova lista vuota
            inventario = new ArrayList<>();
        }
    }

    // metodo che salva i dati su file, in pratica fa l'operazione inversa del metodo sopra.
    // anche qui in caso di errore visualizza un messaggio
    public void salvaProdotti() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileProdotti))) {
            oos.writeObject(inventario);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dei prodotti: " + e.getMessage());
        }
    }

    // metodo che restituisce una List con tutti i prodotti
    public List<Prodotto> prendiTuttiProdotti() {
        return new ArrayList<>(inventario);
    }

    // metodo che restituisce i prodotti di un venditore dato l'id
    public List<Prodotto> cercaProdottiDatoVenditore(String idVenditore) {
    	// crea una lista vuota
        List<Prodotto> risultati = new ArrayList<>();
        // poi scorre tutti i prodotti nell'inventario e aggiunge alla lista solo quelli
        // con l'idVenditore giusto
        for (Prodotto prodotto : inventario) {
            if (prodotto.getIdVenditore().equals(idVenditore)) {
                risultati.add(prodotto);
            }
        }
        return risultati;
    }

    // metodo che conta i prodotti nell'inventario e restituisce
    // il prossimo id prodotto
    public int prossimoIdProdotto() {
        int maxId = 0;
        for (Prodotto prodotto : inventario) {
            if (prodotto.getId() > maxId) {
                maxId = prodotto.getId();
            }
        }
        return maxId + 1;
    }
    
    // metodo che visualizza la lista ordinata di tutti i prodotti
    public void mostraTuttiProdotti(Scanner scanner, Utente utente, GestoreUtenti gestoreUtenti) {
        System.out.println("\n===== TUTTI I PRODOTTI =====");
        // crea una lista con tutti i prodotti
        List<Prodotto> prodotti = prendiTuttiProdotti();
        
        // se è vuota visualizza un opportuno messaggio
        if (prodotti.isEmpty()) {
            System.out.println("Nessun prodotto disponibile al momento.");
            return;
        }
        
        // visualizza uno ad uno i dettagli di tutti i prodotti in ordine
        for (int i = 0; i < prodotti.size(); i++) {
            Prodotto prodotto = prodotti.get(i);
            System.out.println((i+1) + ". " + prodotto);
        }
        
        // chiede all'utente se vuole aggiungere un prodotto al carrello
        System.out.println("\nVuoi aggiungere un prodotto al carrello? (si/no): ");
        String scelta = scanner.nextLine().trim().toLowerCase();
        
        // se risponde si, chiede quale prodotto vuole aggiungere
        if (scelta.equals("si")) {
        	System.out.println("Inserisci il numero del prodotto da aggiungere al carrello: ");
        	int numeroProdotto = GestioneMenu.getIntInput(scanner);
        	
        	// controlla che il numero inserito dall'utente sia valido
        	if (numeroProdotto <= 0 || numeroProdotto > prodotti.size() ) {
        		System.out.println("Numero prodotto non valido.");
        		return;
        	}
        	
        	// aggiunge il prodotto al carrello richiamando il metodo
        	GestoreCarrello.aggiungiProdottoAlCarrello(utente, gestoreUtenti, prodotti.get(numeroProdotto - 1));
        }
        
    }
    
    // cerca i prodotti che contengono nel nome la parola cercata
    public void cercaProdottoPerNome(Scanner scanner, Utente utente, GestoreUtenti gestoreUtenti) {
        System.out.println("\n===== CERCA PRODOTTO =====");
        System.out.print("Inserisci il nome del prodotto da cercare: ");
        String parolaChiave = scanner.nextLine();
        
        List<Prodotto> risultati = new ArrayList<Prodotto>();
        
        // se la parola è contenuta nel nome del prodotto lo aggiunge ai risultati
        for ( Prodotto prodotto : inventario ) {
        	if ( prodotto.getNome().toLowerCase().contains(parolaChiave.toLowerCase()) ){
        		risultati.add(prodotto);
        	}
        }
        
        if (risultati.isEmpty()) {
            System.out.println("Nessun prodotto trovato con '" + parolaChiave + "'.");
            return;
        }
        
        // mostra i prodotti che rispecchiano i criteri della ricerca
        System.out.println("\nRisultati della ricerca:");
        for (int i = 0; i < risultati.size(); i++) {
            Prodotto prodotto = risultati.get(i);
            System.out.println((i+1) + ". " + prodotto);
        }
        
        // chiede all'utente se vuole aggiungerne uno al carrello
        System.out.println("\nVuoi aggiungere uno dei prodotti al carrello? (si/no): ");
        String scelta = scanner.nextLine().trim().toLowerCase();
        
        if (scelta.equals("si")) {
        	System.out.println("Inserisci il numero del prodotto da aggiungere al carrello: ");
        	int numeroProdotto = GestioneMenu.getIntInput(scanner);
        	
        	// controlla che il numero inserito dall'utente sia valido
        	if (numeroProdotto <= 0 || numeroProdotto > risultati.size() ) {
        		System.out.println("Numero prodotto non valido.");
        		return;
        	}
        	
        	// aggiunge il prodotto al carrello richiamando il metodo
        	GestoreCarrello.aggiungiProdottoAlCarrello(utente, gestoreUtenti, risultati.get(numeroProdotto - 1));
        }
    }
    
    // metodo che contiene la logica per l'acquisto di un prodotto
    public void acquistaProdotto(Scanner scanner, Utente utenteLoggato, GestoreUtenti gestoreUtenti) {
        System.out.println("\n===== ACQUISTA PRODOTTO =====");
        // crea una lista con tutti i prodotti
        List<Prodotto> prodottiDisponibili = prendiTuttiProdotti();
        
        // rimuove i prodotti esauriti
        prodottiDisponibili.removeIf(p -> p.getQuantita() <= 0);
        
        if (prodottiDisponibili.isEmpty()) {
            System.out.println("Nessun prodotto disponibile per l'acquisto.");
            return;
        }
        
        // visualizza i prodotti disponibili
        System.out.println("Prodotti disponibili:");
        for (int i = 0; i < prodottiDisponibili.size(); i++) {
            Prodotto prodotto = prodottiDisponibili.get(i);
            System.out.println((i+1) + ". " + prodotto);
        }
        
        System.out.println("\nInserisci il numero del prodotto che vuoi acquistare (0 per annullare): ");
        int sceltaUtente = GestioneMenu.getIntInput(scanner);
        
        if (sceltaUtente == 0) {
            System.out.println("Acquisto annullato.");
            return;
        }
        
        if (sceltaUtente < 1 || sceltaUtente > prodottiDisponibili.size()) {
            System.out.println("Scelta non valida.");
            return;
        }
        
        // prende il prodotto scelto e lo salva 
        Prodotto prodottoScelto = prodottiDisponibili.get(sceltaUtente - 1);
        
        // Non permettere l'acquisto dei propri prodotti
        if (prodottoScelto.getIdVenditore().equals(utenteLoggato.getUsername())) {
            System.out.println("Non puoi acquistare i tuoi stessi prodotti.");
            return;
        }
        
        // chiede all'utente di confermare l'acquisto
        System.out.println("Hai selezionato: " + prodottoScelto);
        System.out.println("Confermi l'acquisto? (si/no): ");
        String conferma = scanner.nextLine().trim().toLowerCase();
        
        if (conferma.equals("si")) {
        	// controlla che l'utente abbia abbastanza soldi per effettuare l'acquisto
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
            salvaProdotti();
            
            System.out.println("Acquisto completato con successo!");
        } else {
            System.out.println("Acquisto annullato.");
        }
    }
    
    // metodo per mettere in vendita un prodotto
    public void vendiProdotto(Scanner scanner, Utente utenteLoggato) {
        System.out.println("\n===== METTI IN VENDITA UN PRODOTTO =====");
        
        // chiede all'utente di inserire i dettagli del prodotto
        System.out.println("Nome del prodotto: ");
        String nome = scanner.nextLine();
        System.out.println("Descrizione: ");
        String descrizione = scanner.nextLine();
        System.out.println("Prezzo (€): ");
        double prezzo = GestioneMenu.getDoubleInput(scanner);
        System.out.println("Quantità disponibile: ");
        int quantita = GestioneMenu.getIntInput(scanner);
        
        // controlla che la quantità sia intera e positiva
        if (quantita <= 0) {
            System.out.println("La quantità deve essere un numero intero, maggiore di zero.");
            return;
        }
        
        // crea un nuovo prodotto con i dati inseriti dall'utente usando il costruttore
        Prodotto nuovoProdotto = new Prodotto(prossimoIdProdotto(), nome, descrizione, prezzo, quantita, utenteLoggato.getUsername() );
        
        // infine aggiunge il prodotto all'inventario e salva
        inventario.add(nuovoProdotto);
        salvaProdotti();
        
        System.out.println("Prodotto aggiunto con successo!");
    }
    
    // mostra un lista con i prodotti i propri prodotti in vendita
    public void mostraMieiProdotti(Utente utenteLoggato) {
        System.out.println("\n===== I MIEI PRODOTTI IN VENDITA =====");
        List<Prodotto> mieiProdotti = cercaProdottiDatoVenditore(utenteLoggato.getUsername());
        
        if (mieiProdotti.isEmpty()) {
            System.out.println("Non hai prodotti in vendita.");
            return;
        }
        
        for (int i = 0; i < mieiProdotti.size(); i++) {
            Prodotto prodotto = mieiProdotti.get(i);
            System.out.println((i+1) + ". " + prodotto + " ");
        }
    }
}
