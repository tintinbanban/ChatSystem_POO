package com.chat_system.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.chat_system.controller.Controller;
import communication.Message;
import communication.User;

/**
 * Classe qui se joint a un groupe Multicast et cree un socket d'envoi de
 * donnees associe
 * 
 * @author Jacquouille
 * 
 */
public class MulticastSendSocket extends ChatMulticastSocket implements
		Runnable {
	/**
	 * Le ttl associe au socket (time to live) = 1 : reseau local.
	 */
	private int ttl = 1;

	// //////////////////////////////////////////
	// Constructeur(s)
	/**
	 * Instanciation d'un MulticastSendSocket
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
	public MulticastSendSocket(int port, String group, Controller controller) {
		super(port, group, controller);
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	// //////////////////////////////////////////
	// Methode(s) specifique(s)
	public void initSendSocket() {
		try {
			// -- Creation du socket multicast
			setS(new MulticastSocket(getPort()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DatagramPacket getUserDp() {
		DatagramPacket dP = null;
		try {
			// -- Conversion d'un objet 'User' en byte (pour l'envoi de
			// paquets)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(getController().getLocalUser());
			oos.flush();
			byte[] buff = baos.toByteArray();
			dP = new DatagramPacket(buff, buff.length,
					InetAddress.getByName(getGroup()), getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dP;
	}

	public void sendUser() {
		try {
			DatagramPacket dP = getUserDp();
			// -- Envoi du paquet
			getS().send(dP);
//			System.out.println("[" + getPseudoUser()
//					+ "@MulticastSendSocket] SendUser from "
//					+ getPseudoUser() + "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void closeSendSocket() {
		// -- On quitte le groupe multicast et on ferme le socket
		getS().close();
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)
	public void run() {
		while (true) {
			try {
				// -- Envoi periodique d'un User sur le chat (utilisateur
				// connecte)
				Thread.sleep(3000);
				sendUser();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
