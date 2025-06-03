package org.java.snacks.ecommerce.gestione;
import java.io.*;
import java.util.*;

import org.java.snacks.ecommerce.modelli.Utente;



public class GestoreUtenti {
	// variabili di istanza
	private List<Utente> utentiRegistrati;
    private String fileUtenti;

    // costruttore
    public GestoreUtenti(String nomeFile) {
        this.fileUtenti = nomeFile;
        this.utentiRegistrati = new ArrayList<>();
    }

    // metodo per caricare i dati salvati su file.
    // Prende i dati dal file -> li converte in una Lista -> li mette in utentiRegistrati
    // se avviene un'eccezione la gestisce visualizzando l'errore e inizializzando una lista vuota
    public void caricaUtenti() {
        File file = new File(fileUtenti);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            utentiRegistrati = (ArrayList<Utente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento degli utenti: " + e.getMessage());
            // In caso di errore, inizializza una nuova lista vuota
            utentiRegistrati = new ArrayList<>();
        }
    }

    // metodo per salvare i dati su file.
    public void salvaUtenti() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileUtenti))) {
            oos.writeObject(utentiRegistrati);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio degli utenti: " + e.getMessage());
        }
    }
    
    // metodo per gestire l'autenticazione utente.
    // in pratica se username e password corrispondono a quelle di un utente restituisce l'utente
    // altrimenti restituisce null
    public Utente autenticazione(String username, String password) {
        for (Utente utente : utentiRegistrati) {
            if (utente.getUsername().equals(username) && utente.getPassword().equals(password)) {
                return utente;
            }
        }
        return null;
    }

    // metodo che controlla se esiste un'utente dato username
    public boolean esisteUtente(String username) {
        for (Utente utente : utentiRegistrati) {
            if (utente.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // metodo per aggiungere un utente agli utentiRegistrati
    public void aggiungiUtente(Utente utente) {
        utentiRegistrati.add(utente);
    }
    
    // metodo che restituisce la lista degli utenti registrati
    public List<Utente> getUtentiRegistrati(){
    	return this.utentiRegistrati;
    }
}
