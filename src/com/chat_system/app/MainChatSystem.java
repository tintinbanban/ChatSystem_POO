package com.chat_system.app;

import java.net.SocketException;
import java.net.UnknownHostException;

public class MainChatSystem {
	/**
	 * @param args
	 */
	// //////////////////////////////////////////
	// METHODE TEST
	public static void main(String[] args) {
		// -- Instances de chat (fenetres)
		ChatSystem chatU;

		try {
			// -- Lancement d'une fenetre
			chatU = ChatSystem.getInstance();
			new Thread(chatU.getGui()).run();
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
