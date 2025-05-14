package org.java.snacks.ecommerce.modelli;
import java.io.Serializable;  // interfaccia che permette la serializzazione della classe 
import java.util.ArrayList;
import java.util.List;

public class Utente implements Serializable{
	private static final long serialVersionUID = 1L; // serve per la serializzazione
	
	// variabili di instanza
	private String username;
	private String password;
	private List<Prodotto> carrello;
	private double saldo;
	
	// costruttore
	public Utente(String username, String password) {
		this.username = username;
		this.password = password;
		this.carrello = new ArrayList<>();  // inizializza una list vuota
		this.saldo = 0.00;
	}
	
	// metodi getter e setter per accedere ai dati
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Prodotto> getCarrello(){
		return this.carrello;
	}
	public void setCarrello(List<Prodotto> carrello) {
		this.carrello = carrello;
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	// metodi per il saldo
	public void effettuaRicarica(double importo) {
		if (importo > 0.00) {
			this.saldo += importo;
		}
	}
	
	public void effettuaPagamento(double spesa) {
		if (spesa > 0.00 && spesa <= saldo) {
			this.saldo -= spesa;
		}
	}
	
	// metodi per il carrello
	public void aggiungiAlCarrello(Prodotto prodotto) {
		if (prodotto != null) {
			carrello.add(prodotto);
		}
	}
	public void rimuoviDalCarrello(Prodotto prodotto) {
		if (prodotto != null) {
			carrello.remove(prodotto);
		}
	}
}
