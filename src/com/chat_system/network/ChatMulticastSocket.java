package com.chat_system.network;

import java.net.MulticastSocket;

import com.chat_system.controller.Controller;

public abstract class ChatMulticastSocket {
	// //////////////////////////////////////////
	// Attribut(s)
	/**
	 * Le port d'ecoute
	 */
	private int port;
	/**
	 * L'adresse de multicast
	 */
	private String group;
	/**
	 * Le socket Multicast
	 */
	private MulticastSocket s;
	/**
	 * Le controller dependant des sockets multicast
	 */
	private Controller controller;
	/**
	 * Le pseudo de l'utilisateur du chat
	 */
	private String pseudoUser;

	// //////////////////////////////////////////
	// Constructeur(s)
	/**
	 * Instanciation d'un ChatMulticastSocket
	 * 
	 * @param port
	 *            - le port d'ecoute
	 * @param group
	 *            - l'adresse IP du groupe Multicast
	 * @param controller
	 *            - le controller de l'application
	 * 
	 *            Par defaut, le socket Multicast est initialise a 'null'
	 */
	public ChatMulticastSocket(int port, String group, Controller controller) {
		super();
		this.port = port;
		this.group = group;
		this.controller = controller;
		this.pseudoUser = controller.getLocalUser().getPseudo();
		this.s = null;
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public MulticastSocket getS() {
		return s;
	}

	public void setS(MulticastSocket s) {
		this.s = s;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public String getPseudoUser() {
		return pseudoUser;
	}

	public void setPseudoUser(String pseudoUser) {
		this.pseudoUser = pseudoUser;
	}
}
