package com.chat_system.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.chat_system.controller.Controller;
import communication.User;

/**
 * Classe qui se joint a un groupe Multicast et cree un socket de reception de
 * donnees associe
 * 
 * @author Jacquouille
 * 
 */
public class MulticastReceiveSocket extends ChatMulticastSocket implements
		Runnable {
	// //////////////////////////////////////////
	// Constructeur(s)
	/**
	 * Instanciation d'un MulticastReceiveSocket
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
	public MulticastReceiveSocket(int port, String group, Controller controller) {
		super(port, group, controller);
	}

	// //////////////////////////////////////////
	// Methode(s) specifique(s)
	public void initReceiveSocket() {
		try {
			// -- Creation du socket multicast ecoutant sur le port 'port'
			super.setS(new MulticastSocket(super.getPort()));

			// -- Abonnement au groupe multicast
			super.getS().joinGroup(InetAddress.getByName(super.getGroup()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeReceiveSocket() {
		// -- On quitte le groupe multicast et on ferme le socket
		try {
			getS().leaveGroup(InetAddress.getByName(super.getGroup()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		getS().close();
	}

	public void processUDPPacket(DatagramPacket dP) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(dP.getData());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			// -- Cas ou le contenu du paquet est un MessageUser
			if (o instanceof User) {
				User user = (User) o;
				// -- Appel au protocole processMessageUser s'il est different du
				// localUser
				if (!user.equals(getController().getLocalUser())) {
					getController().processUser(user);
				}
			} else {
				// -- Lancement d'une exception du ChatSystem
				System.out
						.println("__[ChatSystemException !@MulticastReceiveSocket/processUDPPacket] : type de paquet reçu inconnu...__");
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)
	public void run() {
		DatagramPacket dP;
		byte[] byteMsg;

		// -- Avant de demarrer la boucle infinie,
		// on attend que le Thread de l'interface graphique ait instancie tous
		// les composants
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (true) {
			byteMsg = new byte[1024];
			dP = new DatagramPacket(byteMsg, byteMsg.length);
			try {
	
				getS().receive(dP);
				// -- Reception d'un paquet
				// System.out.println("[" + getPseudoUser()
				// + "@MulticastReceiveSocket] Received '"
				// + (new String(byteMsg, 0, dP.getLength())) + "'");
				// -- Traitement du paquet
				processUDPPacket(dP);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
