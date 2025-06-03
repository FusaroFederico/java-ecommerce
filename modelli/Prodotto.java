package org.java.snacks.ecommerce.modelli;
import java.io.Serializable;

/**
 * Implementa l'interfaccia Serializable per poter essere salvata su file.
 */
public class Prodotto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// variabili di istanza
	private int id;
	private String nome;
	private String descrizione;
	private double prezzo;
	private int quantita;
	private String categoria;
	private String idVenditore;
	
	// costruttore
	public Prodotto(int id, String nome, String descrizione, double prezzo, int quantita, String idVenditore) {
		this.id = id;
		this.nome = nome;
		this.descrizione = descrizione;
		this.prezzo = prezzo;
		this.quantita = quantita;
		this.categoria = "Generico";
		this.idVenditore = idVenditore;
	}

	// metodi per accedere alle variabili get e set
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}
	
	public int getQuantita() {
		return this.quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getIdVenditore() {
		return this.idVenditore;
	}
	
	public void setIdVenditore(String venditore) {
		this.idVenditore = venditore;
	}
	
	public void decrementaQuantita() {
		if(quantita > 0) {
			quantita--;
		}
	}
	
	// metodo toString per visualizzare le info dei prodotti
	@Override
	public String toString() {
		return "Nome: " + this.nome + ", Descrizione: " + this.descrizione + ", Prezzo: " + this.prezzo + "€ , Categoria: " + 
					this.categoria + ", Disponibilità: " + this.quantita + " pezzi.";
	}
	
}
