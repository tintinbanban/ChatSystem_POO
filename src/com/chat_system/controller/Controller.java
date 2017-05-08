package com.chat_system.controller;

import java.io.IOException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Timer;

import com.chat_system.app.ChatSystem;
import com.chat_system.model.UserList;
import com.chat_system.network.MulticastReceiveSocket;
import com.chat_system.network.MulticastSendSocket;
import com.chat_system.network.TCPCLientSocket;
import com.chat_system.network.TCPServerSocket;
import com.chat_system.view.GUI;

import communication.Message;
import communication.User;

/**
 * Classe gerant les interactions entre l'utilisateur du chatSystem et la
 * RemoteApp __ utilisation du pattern *MVC*
 * 
 * @author Jacquouille
 * 
 */
public class Controller implements Observable {
	// //////////////////////////////////////////
	// Attribut(s)
	private UserList userList;
	private User localUser;
	private GUI gui;
	private ArrayList<Object> tabObservateurs;
	private String receivedMessage = null;
	private User expUser = null;
	private MulticastReceiveSocket sReceive;
	private MulticastSendSocket sSend;
	private TCPServerSocket sTCPserver;
	private TCPCLientSocket sTCPclient;
	private Thread thReceive;
	private Thread thSend;
	// -- Variables (connexion multicast)
	final int PORT_MULTI = 5002;	// 5002
	final String GROUP_MULTI = "225.1.2.3"; // 225.1.2.3 
	final int RANDOM_PORT = (int) ((60000 - 20000) * Math.random() + 20000); // generation
																				// aleatoire
																				// d'un
																				// numero
																				// de
																				// port

	// -- Enumerations
	public static enum method_name {
		processUser, processMessage
	}
	private Timer ctrlTimeout;
	
	// //////////////////////////////////////////
	// Constructeur(s)
	public Controller(UserList userList, GUI gui) {
		setUserList(userList);
		setGui(gui);
		tabObservateurs = new ArrayList<>();
		ctrlTimeout = null;
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	public GUI getGui() {
		return gui;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public User getLocalUser() {
		return localUser;
	}

	public void setLocalUser(User localUser) {
		this.localUser = localUser;
	}

	public String getReceivedMessage() {
		return receivedMessage;
	}

	public void setReceivedMessage(String receivedMessage) {
		this.receivedMessage = receivedMessage;
	}

	public User getExpUser() {
		return expUser;
	}

	public void setExpUser(User expUser) {
		this.expUser = expUser;
	}

	public MulticastReceiveSocket getsReceive() {
		return sReceive;
	}

	public void setsReceive(MulticastReceiveSocket sReceive) {
		this.sReceive = sReceive;
	}

	public MulticastSendSocket getsSend() {
		return sSend;
	}

	public void setsSend(MulticastSendSocket sSend) {
		this.sSend = sSend;
	}

	public TCPServerSocket getsTCPserver() {
		return sTCPserver;
	}

	public void setsTCPserver(TCPServerSocket sTCPserver) {
		this.sTCPserver = sTCPserver;
	}

	public TCPCLientSocket getsTCPclient() {
		return sTCPclient;
	}

	public void setsTCPclient(TCPCLientSocket sTCPclient) {
		this.sTCPclient = sTCPclient;
	}

	public Thread getThReceive() {
		return thReceive;
	}

	public void setThReceive(Thread thReceive) {
		this.thReceive = thReceive;
	}

	public Thread getThSend() {
		return thSend;
	}

	public void setThSend(Thread thSend) {
		this.thSend = thSend;
	}

	public Timer getCtrlTimeout() {
		return ctrlTimeout;
	}

	public void setCtrlTimeout(Timer ctrlTimeout) {
		this.ctrlTimeout = ctrlTimeout;
	}

	// //////////////////////////////////////////
	// Methode(s) specifique(s)
	// __ Methode d'etablissement de connexion a la RemoteApp
	public void performConnect(String pseudo) {
		int unPort = RANDOM_PORT;
		try {
			localUser = new User(pseudo, InetAddress.getLocalHost(), unPort, User.typeConnect.CONNECTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// -- Ouverture d'un socket multicast pour envoyer des donnees
		sSend = new MulticastSendSocket(PORT_MULTI, GROUP_MULTI, this);
		sSend.initSendSocket();
		// -- Ouverture d'un socket multicast pour recevoir des donnees
		sReceive = new MulticastReceiveSocket(PORT_MULTI, GROUP_MULTI, this);
		sReceive.initReceiveSocket();
		// -- Ouverture d'un TCPserverSocket et TCPClientSocket pour gerer une
		// connexion TCP
		sTCPserver = new TCPServerSocket(unPort, this);
		sTCPserver.initTCPServerSocket();
		sTCPclient = new TCPCLientSocket(this);
		// -- Lancement des threads associes aux sockets multicast
		thSend = new Thread(sSend);
		thSend.start();
		thReceive = new Thread(sReceive);
		thReceive.start();
		// -- Lancement du Timer de controle des utilisateurs : Reveil toutes les 5 secondes
		ctrlTimeout = new Timer();
		ctrlTimeout.schedule(new TimeoutController(this), 1000, 6000);
		// -- Lancement du thread d'ecoute TCPServer en attente d'une
		// communication
		sTCPserver.start();
	}

	// __ Methode de deconnexion a la RemoteApp
	public void performDisconnect() throws IOException {
		// -- Informer les utilisateurs de notre deconnexion
		localUser.setEtat(User.typeConnect.DECONNECTED);
		sSend.sendUser();
		// -- Fermeture des threads associes aux sockets multicasts
		thSend.stop();
		thReceive.stop();
		ctrlTimeout.cancel();
		// -- Fermeture des sockets multicast
		getsReceive().closeReceiveSocket();
		getsSend().closeSendSocket();
		// -- Gestion de fermeture des sockets TCP et threads TCP
		if (sTCPserver.getsServer() != null)
			sTCPserver.closeTCPServerSocket();
		sTCPserver.stop(); // -- thread d'ecoute TCP
		if (sTCPclient.getsClient() != null)
			sTCPclient.closeTCPClientSocket();
	}

	// __ Methode d'envoi d'un message a la RemoteApp
	public void performMessage(String info, User dest)
			throws IOException {
		// ///////////////////////////////////////
		// -- ENVOI en TCP
		// -- Etablissement d'une connexion TCP avec 'dest'
		sTCPclient.initTCPClientSocket(dest.getIP(), dest.getPort());
		sTCPclient.sendMessage(info);
	}

	// __ Methode de traitement d'un message venant de la RemoteApp
	public void processMessage(Message message, InetAddress ipExp)
			throws IOException {
		// ///////////////////////////////////////
		// -- RECEPTION en TCP
		// -- Recuperation du message et recherche de l'identite de l'expediteur
		expUser = getUserList().getUserByIP(ipExp.getHostAddress());
		if (expUser != null) {
			receivedMessage = message.getData();
			// -- Notification de la reception du message a la vue
			notifierObservateurs(method_name.processMessage.name());
			// -- Reinitialisation des valeurs
			receivedMessage = null;
			expUser = null;
		} else {
			// -- Lancement d'une exception du ChatSystem
			System.out
					.println("__[ChatSystemException !@Controller/processMessage] : User expediteur non reconnu...__");
		}
	}

	// __ Methode de traitement d'un messageUser venant de la RemoteApp
	public void processUser(User user) throws IOException {
		// -- ... Est-ce que l'on recoit un message de deconnexion ?
		if (user.getEtat() == User.typeConnect.DECONNECTED) {
			// --> oui, donc on supprime l'utilisateur de la liste
			getUserList().removeUser(user);
		} else {
			//--> non... Est-ce un nouvel utilisateur ?
			if (getUserList().getUser(user) == null) {
				// -- > oui, alors on l'ajoute a la liste
				getUserList().addUser(user);
			}
			else {
				// --> non, on met a jour le timeout
				getUserList().updateTimeout(user);
			}
		}
		notifierObservateurs(method_name.processUser.name());
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)
	@Override
	public void ajouterObservateur(Observateur o) {
		this.tabObservateurs.add(o);
	}

	@Override
	public void supprimerObservateur(Observateur o) {
		this.tabObservateurs.remove(o);
	}

	@Override
	public void notifierObservateurs(String method) {
		for (Object o : this.tabObservateurs) {
			((Observateur) o).actualiser(this, method);
		}
	}

	// //////////////////////////////////////////
	// Methode(s) statique(s)
	// ...
}
