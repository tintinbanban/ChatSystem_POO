package com.chat_system.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.chat_system.controller.Controller;
import communication.Message;

public class TCPCLientSocket {
	// //////////////////////////////////////////
	// Attribut(s)
	/**
	 * Le port d'ecoute
	 */
	private int port;
	/**
	 * Le controller dependant des sockets
	 */
	private Controller controller;
	/**
	 * Le pseudo de l'utilisateur du chat
	 */
	private String pseudoUser;
	/**
	 * Le serveur TCP auquel on veut se connecter
	 */
	private InetAddress server;
	/**
	 * Le socket associe au client
	 */
	private Socket sClient;
	private TCPCommunication tcp;

	// //////////////////////////////////////////
	// Constructeur(s)
	/**
	 * Instanciation d'un TCPServerSocket
	 * 
	 * @param port
	 *            - le port d'ecoute
	 * @param controller
	 *            - le controller de l'application
	 * 
	 *            Par defaut, le socket Multicast est initialise a 'null'
	 */
	public TCPCLientSocket(Controller controller) {
		this.controller = controller;
		this.pseudoUser = controller.getLocalUser().getPseudo();
		this.server = null;
		this.sClient = null;
		this.tcp = null;
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

	public InetAddress getServer() {
		return server;
	}

	public void setServer(InetAddress server) {
		this.server = server;
	}

	public Socket getsClient() {
		return sClient;
	}

	public void setsClient(Socket sClient) {
		this.sClient = sClient;
	}

	public TCPCommunication getTcp() {
		return tcp;
	}

	public void setTcp(TCPCommunication tcp) {
		this.tcp = tcp;
	}

	// //////////////////////////////////////////
	// Methode(s) specifique(s)
	// __ Methode d'initiation d'une connexion TCP de la part d'un client
	// ____ a un serveur
	public void initTCPClientSocket(InetAddress server, int port) {
		try {
			// -- Creation du clientSocket se connectant au serveur a l'adresse
			// 'IP' et
			// -- ecoutant sur le port 'port'
			setServer(server);
			setsClient(new Socket(getServer(), port));
			// -- Initialiser les flux d'informations entre client et serveur
			tcp = new TCPCommunication(controller, sClient);
//			System.out.println("[TCPClient@" + getPseudoUser() + ","
//					+ +getPort() + "|j'ai demande une connexion a l'adresse :"
//					+ sClient.getInetAddress().getHostAddress()
//					+ "| sur le port :" + sClient.getPort() + "]");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String info) {
		if (sClient != null) {
			if (tcp != null) {
				tcp.initTCPCommunication(true);
				try {
					// -- Creation du message a vehiculer
					Message message = new Message(info, getController().getLocalUser());
					// -- Envoi message
					tcp.getOut().writeObject(message);
					tcp.getOut().flush();
					// -- Fermeture connexion
					tcp.closeTCPCommunication();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void closeTCPClientSocket() {
		try {
			getsClient().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)
}
