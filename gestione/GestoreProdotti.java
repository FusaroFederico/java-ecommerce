package org.java.snacks.ecommerce.gestione;
import java.io.*;
import java.util.*;

import org.java.snacks.ecommerce.modelli.Prodotto;

public class GestoreProdotti {
	// variabili di istanza
	private List<Prodotto> inventario;
    private String fileProdotti;
    
    public GestoreProdotti(String nomeFile) {
        this.fileProdotti = nomeFile;
        this.inventario = new ArrayList<>();
    }

    public void caricaProdotti() {
        File file = new File(fileProdotti);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            inventario = (ArrayList<Prodotto>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento dei prodotti: " + e.getMessage());
            // In caso di errore, inizializza una nuova lista vuota
            inventario = new ArrayList<>();
        }
    }

    public void salvaProdotti() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileProdotti))) {
            oos.writeObject(inventario);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dei prodotti: " + e.getMessage());
        }
    }

    public void aggiungiProdotto(Prodotto prodotto) {
        inventario.add(prodotto);
    }

    public List<Prodotto> prendiTuttiProdotti() {
        return new ArrayList<>(inventario);
    }

    public List<Prodotto> cercaProdotti(String parolaChiave) {
        List<Prodotto> risultati = new ArrayList<>();
        for (Prodotto prodotto : inventario) {
            if ((prodotto.getNome().toLowerCase().contains(parolaChiave.toLowerCase()) || 
                prodotto.getDescrizione().toLowerCase().contains(parolaChiave.toLowerCase()))) {
                risultati.add(prodotto);
            }
        }
        return risultati;
    }

    public List<Prodotto> cercaProdottiDatoVenditore(String idVenditore) {
        List<Prodotto> risultati = new ArrayList<>();
        for (Prodotto prodotto : inventario) {
            if (prodotto.getIdVenditore().equals(idVenditore)) {
                risultati.add(prodotto);
            }
        }
        return risultati;
    }

    public int prossimoIdProdotto() {
        int maxId = 0;
        for (Prodotto prodotto : inventario) {
            if (prodotto.getId() > maxId) {
                maxId = prodotto.getId();
            }
        }
        return maxId + 1;
    }
}
