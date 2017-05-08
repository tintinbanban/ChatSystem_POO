package com.chat_system.controller;


/**
 *  Interface Observateur définissant le pattern *OBSERVER*
 * @author Jacquouille
 *
 */
public interface Observateur {
	//__ Fonction qui actualise les donnees d'un oberveur fournies par un observable
	void actualiser(Observable o, String method);
}
