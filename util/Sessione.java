package org.java.snacks.ecommerce.util;

import org.java.snacks.ecommerce.modelli.Utente;

public class Sessione {
	private Utente utenteLoggato;

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    public void setUtenteLoggato(Utente utenteLoggato) {
        this.utenteLoggato = utenteLoggato;
    }

    public boolean isLoggedIn() {
        return utenteLoggato != null;
    }

    public void logout() {
        utenteLoggato = null;
    }
}
