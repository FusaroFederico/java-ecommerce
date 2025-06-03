package org.java.snacks.ecommerce.giochi.battagliaNavale;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.java.snacks.ecommerce.modelli.Utente;

public class GestioneSalvataggi {
    private String fileSalvataggi;
    private List<Salvataggio> salvataggi;
    
    // costruttore
    public GestioneSalvataggi(String nomeFile) {
    	this.fileSalvataggi = nomeFile;
    	this.salvataggi = new ArrayList<>();
    }

    // carica i dati da file, se avviene un errore inizializza una lista vuota
    public void caricaSalvataggi() {
        File file = new File(fileSalvataggi);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            salvataggi = (ArrayList<Salvataggio>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento degli utenti: " + e.getMessage());
            // In caso di errore, inizializza una nuova lista vuota
            salvataggi = new ArrayList<>();
        }
    }

    // salva i dati su file
    public void salva() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileSalvataggi))) {
            oos.writeObject(salvataggi);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    // metodo che aggiunge un salvataggio
    public void aggiungiSalvataggio(Salvataggio salvataggio) {
    	salvataggi.add(salvataggio);
    }
    
    // metodo che restituisce il salvataggio di un utente specifico
    public Salvataggio getSalvataggioUtente(Utente utente) {
    	for(Salvataggio salvataggio : salvataggi) {
    		if (salvataggio.getGiocatore().getUsername().equals(utente.getUsername())) {
    			return salvataggio;
    		}
    	}
    	return null;
    }
    
    // get per i salvataggi
    public List<Salvataggio> getSalvataggi(){
    	return this.salvataggi;
    }
    
    // metodo che mostra tutti i salvataggi
    // l'ho usato per fare qualche test
    public void mostraTuttiSalvataggi() {
    	for(Salvataggio s : salvataggi) {
    		System.out.println(s.getGiocatore().getUsername());
    		for(Partita p : s.getPartiteInCorso()) {
    			System.out.println(p.getDataInterruzione());
    		}
    	}
    }
}

