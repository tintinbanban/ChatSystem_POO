package com.chat_system.controller;

/**
 *  Interface Observable definissant le pattern *OBSERVER*
 * @author Jacquouille
 *
 */
public interface Observable {
	//__ Fonction pour ajouter un observateur à la liste
	void ajouterObservateur(Observateur o);
	
	//__ Fonction pour enlever un observateur de la liste
	void supprimerObservateur(Observateur o);
	
	//__ Fonction qui notifie les observateurs d'un changement d'etat de l'observable
	void notifierObservateurs(String method);
}
