package com.chat_system.junit.model;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.chat_system.model.Discussion;
import com.chat_system.model.UserList;
import communication.User;

public class DiscussionTests {
	// -- Variable(s) de classe
	private static User user;
	private static Discussion discTest;

	@BeforeClass
	public static void initDiscussion() throws UnknownHostException {
			user = new User("pseudo", InetAddress.getLocalHost(), -1,
					User.typeConnect.CONNECTED);
	}

	// //////////////////////////////////////////
	// COMPLETE LOCAL DISCUSSION METHOD PART
	@Test
	public void completeLocalDiscussionTest() {
		discTest = new Discussion();

		// NO Discussion
		discTest.completeLocalDiscussion(null);
		assertEquals(
				"[COMPLETELOCALDISCUSSION] Null value on an empty instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		discTest.completeLocalDiscussion("");
		assertEquals(
				"[COMPLETELOCALDISCUSSION] Empty value on an empty instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		discTest.completeLocalDiscussion("coucou");
		assertEquals(
				"[COMPLETELOCALDISCUSSION] Val 'coucou' on an empty instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);

		// YES Discussion
		discTest = new Discussion(user);
		discTest.completeDiscussion(null);
		assertEquals(
				"[COMPLETELOCALDISCUSSION] Null value on an instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		discTest.completeLocalDiscussion("");
		assertEquals(
				"[COMPLETELOCALDISCUSSION] Empty value on an instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		discTest.completeLocalDiscussion("coucou");
		assertEquals(
				"[COMPLETELOCALDISCUSSION] Val 'coucou' on an instance of Discussion...",
				discTest.getDiscussion().toString()
						.equals("-->[MOI]:  coucou\n"), true);

		// APPEND Discussion
		discTest.completeLocalDiscussion("coucou");
		assertEquals(
				"[COMPLETELOCALDISCUSSION] Append val 'coucou' on an instance of Discussion...",
				discTest.getDiscussion().toString()
						.equals("-->[MOI]:  coucou\n-->[MOI]:  coucou\n"), true);
	}

	// //////////////////////////////////////////
	// COMPLETE DISCUSSION METHOD PART
	@Test
	public void completeDiscussionTest() {
		discTest = new Discussion();

		// NO Discussion
		discTest.completeDiscussion(null);
		assertEquals(
				"[COMPLETEDISCUSSION] Null value on an empty instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		discTest.completeDiscussion("");
		assertEquals(
				"[COMPLETEDISCUSSION] Empty value on an empty instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		discTest.completeDiscussion("coucou");
		assertEquals(
				"[COMPLETEDISCUSSION] Val 'coucou' on an empty instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);

		// YES Discussion
		discTest = new Discussion(user);
		discTest.completeDiscussion(null);
		assertEquals(
				"[COMPLETEDISCUSSION] Null value on an instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		discTest.completeDiscussion("");
		assertEquals(
				"[COMPLETEDISCUSSION] Empty value on an instance of Discussion...",
				discTest.getDiscussion().toString().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		discTest.completeDiscussion("coucou");
		assertEquals(
				"[COMPLETEDISCUSSION] Val 'coucou' on an instance of Discussion...",
				discTest.getDiscussion()
						.toString()
						.equals(user.getPseudo()
								+ "("
								+ DateFormat.getTimeInstance(DateFormat.SHORT)
										.format(new Date()) + "):  coucou\n"),
				true);

		// APPEND Discussion
		discTest.completeDiscussion("comment ça va ?");
		assertEquals(
				"[COMPLETEDISCUSSION] Append val 'coucou' on an instance of Discussion...",
				discTest.getDiscussion()
						.toString()
						.equals(user.getPseudo()
								+ "("
								+ DateFormat.getTimeInstance(DateFormat.SHORT)
										.format(new Date())
								+ "):  coucou\n"
								+ user.getPseudo()
								+ "("
								+ DateFormat.getTimeInstance(DateFormat.SHORT)
										.format(new Date())
								+ "):  comment ça va ?\n"), true);
	}
}
