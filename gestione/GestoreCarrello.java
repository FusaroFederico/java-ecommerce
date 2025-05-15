package org.java.snacks.ecommerce.gestione;

import java.util.List;
import java.util.Scanner;

import org.java.snacks.ecommerce.modelli.Prodotto;
import org.java.snacks.ecommerce.modelli.Utente;

public class GestoreCarrello {

	// metodo che mostra il carrello e le relative opzioni
    public static void mostraCarrello(Utente utente, GestoreUtenti gestoreUtenti, GestoreProdotti gestoreProdotti, Scanner scanner) {
        System.out.println("\n===== CARRELLO =====");
        List<Prodotto> carrello = utente.getCarrello();

        // mostra un opportuno messaggio se il carrello è vuoto
        if (carrello.isEmpty()) {
            System.out.println("Il tuo carrello è vuoto.");
            return;
        }

        // mostra una lista con i prodotti nel carrello
        System.out.println("Al momento nel tuo carrello ci sono i seguenti prodotti:");
        for (int i = 0; i < carrello.size(); i++) {
            System.out.println((i + 1) + ". " + carrello.get(i));
        }

        // mostra le opzioni del carrello
        System.out.println("\nOpzioni carrello:");
        System.out.println("1. Rimuovi un prodotto");
        System.out.println("2. Acquista un prodotto");
        // chiede all'utente di scegliere un'opzione
        System.out.print("Scegli un'opzione (0 per uscire): ");
        int scelta = getIntInput(scanner);
        
        // switch per gestire le varie opzioni
        switch (scelta) {
            case 0:
            	// esce dal menù carrello
                return;
            case 1:
                System.out.print("Inserisci il numero del prodotto da rimuovere: ");
                int numeroProdottoRim = getIntInput(scanner);
                // se il numero inserito dall'utente è valido procede alla rimozione
                // altrimenti mostra un oppotruno messaggio
                if (numeroProdottoRim > 0 && numeroProdottoRim <= carrello.size()) {
                    rimuoviProdotto(utente, numeroProdottoRim - 1, gestoreUtenti, scanner);
                } else {
                    System.out.println("Numero prodotto non valido.");
                }
                break;
            case 2:
                System.out.print("Inserisci il numero del prodotto da acquistare: ");
                int numeroProdottoAcq = getIntInput(scanner);
                // se il numero è valido procede con l'acquisto
                // altrimenti mostra un opportuno messaggio
                if (numeroProdottoAcq > 0 && numeroProdottoAcq <= carrello.size()) {
                    acquistaProdotto(utente, carrello.get(numeroProdottoAcq - 1), gestoreUtenti, gestoreProdotti, scanner);
                } else {
                    System.out.println("Numero prodotto non valido.");
                }
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }

    // metodo per la rimozione di un prodotto dal carrello
    private static void rimuoviProdotto(Utente utente, int numeroProdotto, GestoreUtenti gestoreUtenti, Scanner scanner) {
        Prodotto prodotto = utente.getCarrello().get(numeroProdotto);
        // chiede all'utente di confermare 
        System.out.print("Sei sicuro di voler rimuovere " + prodotto.getNome() + "? (si/no): ");
        String conferma = scanner.nextLine().trim().toLowerCase();

        // se è si procede alla rimozione e al salvataggio
        // altrimenti non fa nulla
        if (conferma.equals("si")) {
            utente.rimuoviDalCarrello(prodotto);
            gestoreUtenti.salvaUtenti();
            System.out.println("Prodotto rimosso con successo.");
        }
    }

    private static void acquistaProdotto(Utente utente, Prodotto prodotto, GestoreUtenti gestoreUtenti, GestoreProdotti gestoreProdotti, Scanner scanner) {
        // controlla che il compratore non sia il venditore
    	if (prodotto.getIdVenditore().equals(utente.getUsername())) {
            System.out.println("Non puoi acquistare i tuoi stessi prodotti.");
            return;
        }
    	// controlla che il prodotto non sia esaurito
        if (prodotto.getQuantita() <= 0) {
            System.out.println("Il prodotto è esaurito.");
            return;
        }
        // controlla che l'utente abbia la somma necessaria al pagamento
        if (utente.getSaldo() < prodotto.getPrezzo()) {
            System.out.println("Saldo insufficiente.");
            return;
        }
        
        // chiede all'utente di confermare l'acquisto
        System.out.print("Confermi l'acquisto di " + prodotto.getNome() + "? (si/no): ");
        String conferma = scanner.nextLine().trim().toLowerCase();
        
        // se la risposta è diversa da "si" annulla l'acquisto
        if (!conferma.equals("si")) {
        	System.out.print("Acquisto annullato.");
        	return;
        }
        // altrimenti procede con l'operazione di pagamento
        utente.effettuaPagamento(prodotto.getPrezzo());
        
        // poi cerca il venditore e gli accredita la somma diminuita del 5% ( tassa del sito )
        for (Utente u : gestoreUtenti.getUtentiRegistrati()) {
            if (u.getUsername().equals(prodotto.getIdVenditore())) {
                u.effettuaRicarica(prodotto.getPrezzo() * 0.95);
                break;
            }
        }

        // infine scala di 1 la quantità del prodotto, rimuove il prodotto dal carrello e salva 
        prodotto.decrementaQuantita();
        utente.rimuoviDalCarrello(prodotto);
        gestoreUtenti.salvaUtenti();
        gestoreProdotti.salvaProdotti();
        System.out.println("Acquisto completato con successo.");
    }
    
    private static int getIntInput(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
