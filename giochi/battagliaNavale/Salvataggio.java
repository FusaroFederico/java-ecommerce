package org.java.snacks.ecommerce.giochi.battagliaNavale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.java.snacks.ecommerce.modelli.Utente;

public class Salvataggio implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Utente giocatore;
    private List<Partita> partiteInCorso;
    private List<String> storicoPartite;
    
    // costruttore
    public Salvataggio(Utente giocatore) {
    	this.giocatore = giocatore;
    	this.partiteInCorso = new ArrayList<>();
    	this.storicoPartite = new ArrayList<>();
    }

    // metodo che aggiunge una partita terminata
    // quando arriva a 3, elimina la prima e salva quella più recente
    public void aggiungiPartitaTerminata(Partita partita) {
    	if(storicoPartite.size() == 3) {
    		storicoPartite.remove(0);
    	}
    	storicoPartite.add(partita.toString());
    }
    
    // metodo che permette di salvare le ultime 2 partite interrotte
    // oltre le 2, cancella quella più vecchia
    public void aggiungiPartitaInCorso(Partita partita) {
    	if (partiteInCorso.size() == 2) {
    		partiteInCorso.remove(0);
    	}
    	partiteInCorso.add(partita);
    }
    
    // getter e setter
	public Utente getGiocatore() {
		return giocatore;
	}

	public void setGiocatore(Utente giocatore) {
		this.giocatore = giocatore;
	}

	public List<Partita> getPartiteInCorso() {
		return partiteInCorso;
	}

	public void setPartiteInCorso(List<Partita> partiteInCorso) {
		this.partiteInCorso = partiteInCorso;
	}

	public List<String> getStoricoPartite() {
		return storicoPartite;
	}

	public void setStoricoPartite(List<String> storicoPartite) {
		this.storicoPartite = storicoPartite;
	}
    
    
}