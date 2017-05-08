package com.chat_system.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import com.chat_system.controller.Controller;

public class TCPServerSocket extends Thread {

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
	 * Le serverSocket
	 */
	private ServerSocket sServer;

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
	public TCPServerSocket(int port, Controller controller) {
		this.port = port;
		this.controller = controller;
		this.pseudoUser = controller.getLocalUser().getPseudo();
		this.sServer = null;
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

	public ServerSocket getsServer() {
		return sServer;
	}

	public void setsServer(ServerSocket sServer) {
		this.sServer = sServer;
	}
	
	// //////////////////////////////////////////
	// Methode(s) specifique(s)
	public void initTCPServerSocket() {
		try {
			// -- Creation du serverSocket ecoutant sur le port 'port'
			setsServer(new ServerSocket(getPort()));
			
//			System.out.println("[TCPServer" + getPseudoUser() + "@"
//					+ "|j'ecoute sur le port :"
//					+ getPort() + "]");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeTCPServerSocket() {
		try {
			getsServer().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)
	@Override
	public void run() {
		while (true) {
			try {
				// -- Attente etablissement de connexion ...
				Socket sClient = sServer.accept();
//				System.out.println("[TCPServer@" + getPseudoUser() + ","
//						+ getPort() + "|Connexion avec :"
//						+ sClient.getInetAddress().getHostAddress() + "|" + sClient.getPort() + "]");
				// -- Creation d'un nouveau thread : entierement dedie a la
				// -- communication avec le serveur
				TCPCommunication tcp = new TCPCommunication(controller, sClient);
				Thread th = new Thread(tcp);
				// -- lancement d'un thread d'ecoute
				th.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
