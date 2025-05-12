package org.java.snacks.ecommerce.gestione;
import java.io.*;
import java.util.*;

import org.java.snacks.ecommerce.modelli.Utente;



public class GestoreUtenti {
	// variabili di istanza
	private List<Utente> utentiRegistrati;
    private String fileUtenti;

    public GestoreUtenti(String nomeFile) {
        this.fileUtenti = nomeFile;
        this.utentiRegistrati = new ArrayList<>();
    }

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

    public void salvaUtenti() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileUtenti))) {
            oos.writeObject(utentiRegistrati);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio degli utenti: " + e.getMessage());
        }
    }

    public Utente autenticazione(String username, String password) {
        for (Utente utente : utentiRegistrati) {
            if (utente.getUsername().equals(username) && utente.getPassword().equals(password)) {
                return utente;
            }
        }
        return null;
    }

    public boolean esisteUtente(String username) {
        for (Utente utente : utentiRegistrati) {
            if (utente.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void aggiungiUtente(Utente utente) {
        utentiRegistrati.add(utente);
    }
    
    public List<Utente> getUtentiRegistrati(){
    	return this.utentiRegistrati;
    }
}
