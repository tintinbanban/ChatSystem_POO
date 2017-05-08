package com.chat_system.junit.model;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.chat_system.model.Discussion;
import communication.User;
import communication.User.typeConnect;

public class UserTests {
	// -- Variable(s) de classe
	private static User user, other, nullOther = null;

	// //////////////////////////////////////////
	// EQUALS METHOD PART
	// __ le test d'egalite se fait sur le pseudo et l'adresse IP
	@Test
	public void equalsTest() throws UnknownHostException {
		user = new User("pseudo", InetAddress.getLocalHost(), 1,
				typeConnect.CONNECTED);
		other = new User("other", InetAddress.getLocalHost(), 1,
				typeConnect.CONNECTED);

		assertEquals("Same IP + port + etat comparison...", user.equals(other),
				false);
		// ////////////////////////////////////////////////////////////////////////////////
		other.setPseudo("pseudo");
		other.setIP(InetAddress.getByName("1.1.1.1"));
		assertEquals("Same pseudo + port + etat comparison...",
				user.equals(other), false);
		// ////////////////////////////////////////////////////////////////////////////////
		other.setIP(InetAddress.getLocalHost());
		assertEquals("Same pseudo + IP + port + etat comparison...",
				user.equals(other), true);
		// ////////////////////////////////////////////////////////////////////////////////
		other.setEtat(typeConnect.DECONNECTED);
		assertEquals("Same pseudo + IP + port comparison...",
				user.equals(other), true);
		// ////////////////////////////////////////////////////////////////////////////////
		other.setPort(0);
		assertEquals("Same pseudo + IP comparison...", user.equals(other), true);
		// ////////////////////////////////////////////////////////////////////////////////
		other.setPort(0);
		assertEquals("Same pseudo + IP comparison...", user.equals(other), true);
		// ////////////////////////////////////////////////////////////////////////////////
		other.setIP(InetAddress.getByName("1.1.1.1"));
		assertEquals("Same pseudo comparison...", user.equals(other), false);
		// ////////////////////////////////////////////////////////////////////////////////
		other.setIP(InetAddress.getLocalHost());
		other.setPseudo("other");
		assertEquals("Same IP comparison...", user.equals(other), false);

		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals("Same object comparison...", user.equals(user), true);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals("Null object comparison...", user.equals(null), false);
	}

}
