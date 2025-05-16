package org.java.snacks.ecommerce;
import java.io.*;
import java.util.*;

import org.java.snacks.ecommerce.gestione.GestoreProdotti;
import org.java.snacks.ecommerce.gestione.GestoreUtenti;
import org.java.snacks.ecommerce.gestione.GestioneMenu;
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
        GestioneMenu.mostraMenuIniziale(scanner, utenteLoggato, gestoreUtenti, gestoreProdotti);
        scanner.close();
	}
	
	private static void inizializzazione() {
        scanner = new Scanner(System.in);
        gestoreUtenti = new GestoreUtenti(FILE_UTENTI);
        gestoreProdotti = new GestoreProdotti(FILE_PRODOTTI);
        utenteLoggato = null;
        
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
	
}
