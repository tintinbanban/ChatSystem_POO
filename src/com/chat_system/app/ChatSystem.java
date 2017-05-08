package com.chat_system.app;

import java.net.SocketException;
import java.net.UnknownHostException;

import com.chat_system.controller.Controller;
import com.chat_system.model.UserList;
import com.chat_system.view.GUI;

/**
 * Classe principale de lancement de l'application __ utilisation du pattern
 * *SINGLETON*
 * 
 * @author Jacquouille
 * 
 */
public class ChatSystem {
	// //////////////////////////////////////////
	// Attribut(s)
	private GUI gui;
	private UserList userList;
	private Controller controller;
	private static int nb_instances = 0;

	// //////////////////////////////////////////
	// Constructeur(s)
	private ChatSystem() throws SocketException, UnknownHostException {
		this.userList = new UserList();
		this.controller = new Controller(this.userList, this.gui);
		this.gui = new GUI(this.controller);
		nb_instances++;
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public GUI getGui() {
		return gui;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public static int getNb_instances() {
		return nb_instances;
	}

	// //////////////////////////////////////////
	// Methode(s) statique(s) de classe
	public static ChatSystem getInstance() throws SocketException,
			UnknownHostException {
		return new ChatSystem();
	}
}
