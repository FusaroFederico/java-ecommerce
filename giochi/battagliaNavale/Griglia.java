package org.java.snacks.ecommerce.giochi.battagliaNavale;

import java.io.Serializable;
import java.util.Random;

public class Griglia implements Serializable{
	 
	//Variabili statiche per grandezza della griglia, numero di navi e lunghezza delle navi
	private static final long serialVersionUID = 1L;
	private static final int DIMENSIONE = 5;
    private static final int NUM_NAVI = 3;
    private static final int LUNG_NAVI = 2;

    // array bidimensionali per griglia e posizione delle navi
    private final char[][] griglia = new char[DIMENSIONE][DIMENSIONE];
    private final boolean[][] posizioneNavi = new boolean[DIMENSIONE][DIMENSIONE];

    public Griglia() {
        // Inizializza la griglia vuota
        for (int i = 0; i < DIMENSIONE; i++)
            for (int j = 0; j < DIMENSIONE; j++)
                griglia[i][j] = '~'; // mare
        // Piazza le navi a caso
        piazzaNaviCasualmente(); 
    }

    private void piazzaNaviCasualmente() {
        Random rand = new Random();
        int naviPiazzate = 0;

        while (naviPiazzate < NUM_NAVI) {
        	// sceglie a caso riga e colonna
            int row = rand.nextInt(DIMENSIONE);
            int col = rand.nextInt(DIMENSIONE);
            
            // sceglie a caso la direzione della nave
            boolean horizontal = rand.nextBoolean();
            int r2 = horizontal ? row : row + 1;  // operatore ternario, funziona tipo l'if cioè se è vera la condizione prima del "?"
            									  // usa la prima opzione altrimenti la seconda ( le opzioni sono separate da ":" )
            int c2 = horizontal ? col + 1 : col;

            // se r2 o c2 superano la dimensione della griglia, passa al ciclo successivo senza eseguire il resto delle operazioni
            if (r2 >= DIMENSIONE || c2 >= DIMENSIONE) continue;

            // se nelle celle non c'è già una nave, ce la piazza
            if (!posizioneNavi[row][col] && !posizioneNavi[r2][c2]) {
                posizioneNavi[row][col] = true;
                posizioneNavi[r2][c2] = true;
                naviPiazzate++;
            }
        }
    }
    
    public boolean riceviColpo(int row, int col) {
        if (griglia[row][col] == 'X' || griglia[row][col] == 'O') {
            System.out.println("Hai già sparato qui!");
            return false;
        }

        if (posizioneNavi[row][col]) {
            griglia[row][col] = 'X'; // colpito
            System.out.println("COLPITO!");
            return true;
        } else {
            griglia[row][col] = 'O'; // acqua
            System.out.println("Acqua.");
            return false;
        }
    }

    public boolean tutteLeNaviAffondate() {
    	// verifica se ad ogni nave corrisponde un colpito
        for (int i = 0; i < DIMENSIONE; i++)
            for (int j = 0; j < DIMENSIONE; j++)
                if (posizioneNavi[i][j] && griglia[i][j] != 'X')
                    return false;
        return true;
    }

    // metodo per visualizzare la griglia
    public void mostraGriglia(boolean mostraNavi) {
        System.out.print("  ");
        for (int i = 0; i < DIMENSIONE; i++) System.out.print(i + " ");
        System.out.println();

        for (int i = 0; i < DIMENSIONE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < DIMENSIONE; j++) {
                char cell = griglia[i][j];
                if (mostraNavi && posizioneNavi[i][j] && cell == '~')
                    System.out.print("N ");
                else
                    System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}

