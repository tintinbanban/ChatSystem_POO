package com.chat_system.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.chat_system.controller.Controller;
import communication.Message;

public class TCPCommunication implements Runnable {
	// //////////////////////////////////////////
	// Attribut(s)
	private Controller controller;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	// //////////////////////////////////////////
	// Constructeur(s)
	public TCPCommunication(Controller controller, Socket socket)
			throws IOException {
		this.controller = controller;
		this.socket = socket;
		this.in = null;
		this.out = null;
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	// //////////////////////////////////////////
	// Methode(s) specifique(s)
	public void initTCPCommunication(boolean outputCommunication) {
		// -- Creation d'un flux d'entree ou de sortie
		if (outputCommunication) {
			// -- TCPCLientSocket qui envoit un message
			try {
				setOut(new ObjectOutputStream(socket.getOutputStream()));
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// -- TCPCLientSocket qui envoit un message
			try {
				setIn(new ObjectInputStream(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeTCPCommunication() {
		try {
			in = null;
			out = null;
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receiveMessage(Message message) {
		try {
			InetAddress ipExp = message.getSender().getIP();
			getController().processMessage(message, ipExp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printInfoTCPCommunication() {
		System.out
				.println("\n --- TCPCommunication ---\n  **INFOS LOCALES **\n  *"
						+ controller.getLocalUser().getPseudo()
						+ "@ :\t"
						+ socket.getLocalAddress().getHostName()
						+ "\n  * port :\t"
						+ socket.getLocalPort()
						+ "\n  **INFOS DISTANTES **\n  *  @ :\t"
						+ socket.getInetAddress().getHostAddress()
						+ "\n  * port :\t"
						+ socket.getPort()
						+ "\n  * socket :\t"
						+ socket.getRemoteSocketAddress().toString() + "\n");
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)
	@Override
	public void run() {
		initTCPCommunication(false);
		// -- Si la Socket de communication est ouverte...
		if (!socket.isClosed()) {
			try {
				// -- ... Alors on receptionne l'envoi seulement d'un Objet
				// : Message
				Object o = in.readObject();
				if (o instanceof Message) {
					// Traitement du message
					receiveMessage((Message) o);
				}
				else {
					JOptionPane.showMessageDialog(null, "Type de message inconnu...",
							"Traitement en réception impossible", JOptionPane.ERROR_MESSAGE);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (EOFException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		closeTCPCommunication();
	}
}
