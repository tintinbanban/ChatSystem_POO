package com.chat_system.junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.chat_system.model.Discussion;
import com.chat_system.model.UserList;
import communication.User;

public class UserListTests {
	// -- Variable(s) de classe
	private static UserList uList;
	private static User user, other, nullOther;
	private static Discussion discTest, otherDiscTest;

	@BeforeClass
	public static void initUserList() throws UnknownHostException {
			user = new User("pseudo", InetAddress.getLocalHost(), -1,
					User.typeConnect.CONNECTED);
			other = new User("other", InetAddress.getLocalHost(), -1,
					User.typeConnect.CONNECTED);
			nullOther = new User(null, null, 0, null);
			discTest = new Discussion(user);
			otherDiscTest = new Discussion(other);
	}

	// //////////////////////////////////////////
	// GET USER METHOD PART
	// __ Les differents tests unitaires permettent
	// de valider l'implementation des methodes
	// suivantes : getUser, getUserByIP,
	// getUserFromTCPCommunication, getUserByPseudo
	@Test
	public void getUserTest() {
		uList = new UserList();
		// 0 user
		assertEquals("[GETUSER] Null value on a empty list...",
				uList.getUser(null), null);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals("[GETUSER] Val 'user' on an empty list...",
				uList.getUser(user), null);

		// 1 user
		uList.getUserList().add(user);
		assertEquals(
				"[GETUSER] Val 'user' on a 1 element list (contains 'user')...",
				uList.getUser(user), user);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETUSER] Unknown user on a 1 element list (contains 'user')...",
				uList.getUser(other), null);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETUSER] Null value on a non empty list (contains 'user')...",
				uList.getUser(null), null);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETUSER] Val 'nullOther' on a non empty list (contains 'user')...",
				uList.getUser(nullOther), null);

		// 2 users
		uList.getUserList().add(other);
		assertEquals(
				"[GETUSER] Val 'user' on a 2 elements list (contains 'user' and 'other')...",
				uList.getUser(user), user);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETUSER] Val 'other' on a 2 elements list (contains 'user' and 'other')...",
				uList.getUser(other), other);
	}

	// //////////////////////////////////////////
	// IS DECONNECTING METHOD PART
	@Test
	public void userIsDeconnectingTest() throws UnknownHostException {
		uList = new UserList();
		// 0 user
		assertEquals("[USERISDECONNECTING] Null value on a empty list...",
				uList.userIsDeconnecting(null), false);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals("[USERISDECONNECTING] Val 'user' on an empty list...",
				uList.userIsDeconnecting(user), false);

		// 1 user
		uList.getUserList().add(user);
		assertEquals(
				"[USERISDECONNECTING] Val 'user' (=> CONNECTED) on a 1 element list (contains 'user')...",
				uList.userIsDeconnecting(user), false);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[USERISDECONNECTING] Unknown user on a 1 element list (contains 'user')...",
				uList.userIsDeconnecting(new User("other", null, 2, null)),
				false);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[USERISDECONNECTING] Null value on a non empty list (contains 'user')...",
				uList.userIsDeconnecting(null), false);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[USERISDECONNECTING] Val 'nullOther' on a non empty list (contains 'user')...",
				uList.getUser(nullOther), null);
			// ////////////////////////////////////////////////////////////////////////////////
			assertEquals(
					"[USERISDECONNECTING] Val 'user' (=> DECONNECTED) on a 1 element list (contains 'user')...",
					uList.userIsDeconnecting(new User("pseudo", InetAddress
							.getLocalHost(), -1, User.typeConnect.DECONNECTED)),
					true);

		// 2 users
			uList.getUserList().add(other);
			assertEquals(
					"[USERISDECONNECTING] Val 'user' (=> CONNECTED) on a 2 elements list (contains 'user' and 'other')...",
					uList.userIsDeconnecting(user), false);
			// ////////////////////////////////////////////////////////////////////////////////
			assertEquals(
					"[USERISDECONNECTING] Val 'other' (=> CONNECTED) on a 2 elements list (contains 'user' and 'other')...",
					uList.userIsDeconnecting(other), false);
			other = new User("other", InetAddress.getLocalHost(), -1,
					User.typeConnect.DECONNECTED);
			// ////////////////////////////////////////////////////////////////////////////////
			assertEquals(
					"[USERISDECONNECTING] Val 'other' (=> DECONNECTED) on a 2 elements list (contains 'user' and 'other')...",
					uList.userIsDeconnecting(other), true);
	}

	// //////////////////////////////////////////
	// GET DISCUSSION BY USER METHOD PART
	@Test
	public void getDiscussionByUserTest() {
		uList = new UserList();
		// 0 discussion
		assertEquals("[GETDISCUSSIONBYUSER] Null value on an empty list...",
				uList.getDiscussionByUser(null), null);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals("[GETDISCUSSIONBYUSER] Val 'user' on an empty list...",
				uList.getDiscussionByUser(user), null);

		// 1 empty discussion
		uList.getDiscussionUserList().add(new Discussion());
		assertEquals(
				"[GETDISCUSSIONBYUSER] Null value on an empty discussion in a discussion user list...",
				uList.getDiscussionByUser(null), null);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETDISCUSSIONBYUSER] Val 'user' on an empty discussion in a discussion user list...",
				uList.getDiscussionByUser(user), null);

		// 1 discussion
		uList.getDiscussionUserList().add(discTest);
		assertEquals(
				"[GETDISCUSSIONBYUSER] Null value on a non empty discussion list (contains 'user'...",
				uList.getDiscussionByUser(null), null);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETDISCUSSIONBYUSER] Val 'nullOther' on a non empty list (contains 'user')...",
				uList.getUser(nullOther), null);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETDISCUSSIONBYUSER] Val 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user), discTest);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETDISCUSSIONBYUSER] Unknown val on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(other), null);

		// 2 discussions
		uList.getDiscussionUserList().add(otherDiscTest);
		assertEquals(
				"[GETDISCUSSIONBYUSER] Val 'user' on a 2 elements discussion list (contains 'user' and 'other')...",
				uList.getDiscussionByUser(user), discTest);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[GETDISCUSSIONBYUSER] Val 'other' on a 2 element discussion list (contains 'user' and 'other')...",
				uList.getDiscussionByUser(other), otherDiscTest);
	}

	// //////////////////////////////////////////
	// SET LOCAL DISCUSSION BY USER METHOD PART
	@Test
	public void setLocalDiscussionByUserTest() {
		uList = new UserList();

		// 0 discussion
		uList.setLocalDiscussionByUser(null, null);
		assertEquals(
				"[SETLOCALDISCUSSIONBYUSER] Null values on a null discussion...",
				uList.getDiscussionByUser(null), null);
		// ////////////////////////////////////////////////////////////////////////////////
		uList.setLocalDiscussionByUser("coucou", user);
		assertEquals(
				"[SETLOCALDISCUSSIONBYUSER] Val 'coucou' by unknown user on a null discussion...",
				uList.getDiscussionByUser(user), null);

		// 1 discussion
		uList.getDiscussionUserList().add(new Discussion(user));
		uList.setLocalDiscussionByUser(null, user);
		assertEquals(
				"[SETLOCALDISCUSSIONBYUSER] Null value by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(), "");
		// ////////////////////////////////////////////////////////////////////////////////
		uList.setLocalDiscussionByUser("coucou", user);
		assertEquals(
				"[SETLOCALDISCUSSIONBYUSER] Val 'coucou' by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(),
				"-->[MOI]:  coucou\n");
		// // append text on 1 discussion
		uList.setLocalDiscussionByUser(null, user);
		assertEquals(
				"[SETLOCALDISCUSSIONBYUSER] Append null value by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(),
				"-->[MOI]:  coucou\n");
		// ////////////////////////////////////////////////////////////////////////////////
		uList.setLocalDiscussionByUser("est-ce que tu vas bien ?", user);
		assertEquals(
				"[SETLOCALDISCUSSIONBYUSER] Append val 'est-ce que tu vas bien ?' by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(),
				"-->[MOI]:  coucou\n-->[MOI]:  est-ce que tu vas bien ?\n");
		// ////////////////////////////////////////////////////////////////////////////////
		uList.setLocalDiscussionByUser("", user);
		assertEquals(
				"[SETLOCALDISCUSSIONBYUSER] Append empty string by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(),
				"-->[MOI]:  coucou\n-->[MOI]:  est-ce que tu vas bien ?\n");
	}

	// //////////////////////////////////////////
	// SET DISCUSSION BY USER METHOD PART
	@Test
	public void setDiscussionByUserTest() {
		uList = new UserList();

		// 0 discussion
		uList.setDiscussionByUser(null, null);
		assertEquals(
				"[SETDISCUSSIONBYUSER] Null values on a null discussion...",
				uList.getDiscussionByUser(null), null);
		uList.setDiscussionByUser("coucou", user);
		// ////////////////////////////////////////////////////////////////////////////////
		assertEquals(
				"[SETDISCUSSIONBYUSER] Val 'coucou' by unknown user on a null discussion...",
				uList.getDiscussionByUser(user), null);

		// 1 discussion
		uList.getDiscussionUserList().add(new Discussion(user));
		uList.setDiscussionByUser(null, user);
		assertEquals(
				"[SETDISCUSSIONBYUSER] Null value by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(), "");
		// ////////////////////////////////////////////////////////////////////////////////
		uList.setDiscussionByUser("coucou", user);
		assertEquals(
				"[SETDISCUSSIONBYUSER] Val 'coucou' by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(),
				user.getPseudo()
						+ "("
						+ DateFormat.getTimeInstance(DateFormat.SHORT).format(
								new Date()) + "):  coucou\n");
		// // append text on 1 discussion
		uList.setDiscussionByUser(null, user);
		assertEquals(
				"[SETDISCUSSIONBYUSER] Append null value by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(),
				user.getPseudo()
						+ "("
						+ DateFormat.getTimeInstance(DateFormat.SHORT).format(
								new Date()) + "):  coucou\n");
		// ////////////////////////////////////////////////////////////////////////////////
		uList.setDiscussionByUser("est-ce que tu vas bien ?", user);
		assertEquals(
				"[SETDISCUSSIONBYUSER] Append val 'est-ce que tu vas bien ?' by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(),
				user.getPseudo()
						+ "("
						+ DateFormat.getTimeInstance(DateFormat.SHORT).format(
								new Date())
						+ "):  coucou\n"
						+ user.getPseudo()
						+ "("
						+ DateFormat.getTimeInstance(DateFormat.SHORT).format(
								new Date()) + "):  est-ce que tu vas bien ?\n");
		// ////////////////////////////////////////////////////////////////////////////////
		uList.setDiscussionByUser("", user);
		assertEquals(
				"[SETDISCUSSIONBYUSER] Append empty string by 'user' on a 1 element discussion list (contains 'user')...",
				uList.getDiscussionByUser(user).getDiscussion().toString(),
				user.getPseudo()
						+ "("
						+ DateFormat.getTimeInstance(DateFormat.SHORT).format(
								new Date())
						+ "):  coucou\n"
						+ user.getPseudo()
						+ "("
						+ DateFormat.getTimeInstance(DateFormat.SHORT).format(
								new Date()) + "):  est-ce que tu vas bien ?\n");
	}

	// //////////////////////////////////////////
	// ADD USER METHOD PART
	@Test
	public void addUserTest() {
		uList = new UserList();

		// 0 user
		uList.addUser(null);
		assertEquals(
				"[ADDUSER-USERLIST] Null value added on an empty user list...",
				uList.getUserList().isEmpty(), true);
		assertEquals(
				"[ADDUSER-DISCUSSIONLIST] Null value added on an empty user list...",
				uList.getDiscussionUserList().isEmpty(), true);
		assertEquals(
				"[ADDUSER-TIMEOUTLIST] Null value added on an empty user list...",
				uList.getTimeoutUserList().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		uList.addUser(user);
		assertEquals(
				"[ADDUSER-USERLIST] Val 'user' added on an empty user list...",
				uList.getUserList().contains(user), true);
		assertEquals(
				"[ADDUSER-DISCUSSIONLIST] Val 'user' added on an empty user list...",
				uList.getDiscussionByUser(user).getUser(), user);
		assertEquals(
				"[ADDUSER-TIMEOUTLIST] Val 'user' added on an empty user list...",
				uList.getTimeoutUserList().containsKey(user), true);

		// 1 user
		uList.addUser(null);
		assertEquals(
				"[ADDUSER-USERLIST] Null value added on a 1 element user list...",
				uList.getUserList().size(), 1);
		assertEquals(
				"[ADDUSER-DISCUSSIONLIST] Null value added on a 1 element user list...",
				uList.getDiscussionUserList().size(), 1);
		assertEquals(
				"[ADDUSER-TIMEOUTLIST] Null value added on a 1 element user list...",
				uList.getTimeoutUserList().size(), 1);
		// ////////////////////////////////////////////////////////////////////////////////
		uList.addUser(other);
		assertEquals(
				"[ADDUSER-USERLIST] Val 'other' added on a 1 element user list[val-test]...",
				uList.getUserList().contains(other), true);
		assertEquals(
				"[ADDUSER-USERLIST] Val 'other' added on a 1 element user list[size-test]...",
				uList.getUserList().size(), 2);
		assertEquals(
				"[ADDUSER-DISCUSSIONLIST] Val 'other' added on a 1 element user list[val-test]...",
				uList.getDiscussionByUser(other).getUser(), other);
		assertEquals(
				"[ADDUSER-DISCUSSIONLIST] Val 'other' added on a 1 element user list[size-test]...",
				uList.getDiscussionUserList().size(), 2);
		assertEquals(
				"[ADDUSER-TIMEOUTLIST] Val 'other' added on a 1 element user list[val-test]...",
				uList.getTimeoutUserList().containsKey(other), true);
		assertEquals(
				"[ADDUSER-TIMEOUTLIST] Val 'other' added on a 1 element user list[size-test]...",
				uList.getTimeoutUserList().size(), 2);
	}

	// //////////////////////////////////////////
	// UPDATE TIMEOUT METHOD PART
	@Test
	public void updateTimeoutTest() {
		uList = new UserList();

		// 0 user
		uList.updateTimeout(null);
		assertEquals(
				"[UPDATETIMEOUT] Null value on an empty timeout user list...",
				uList.getTimeoutUserList().isEmpty(), true);
		// ////////////////////////////////////////////////////////////////////////////////
		uList.updateTimeout(user);
		assertEquals(
				"[UPDATETIMEOUT] Val 'user' on an empty timeout user list...",
				uList.getTimeoutUserList().isEmpty(), true);

		// 1 user
		Long putTime = System.currentTimeMillis();
		uList.getTimeoutUserList().put(user, putTime);
		uList.updateTimeout(null);
		assertEquals(
				"[UPDATETIMEOUT] Null value on a 1 element timeout user list[size-test]...",
				uList.getTimeoutUserList().size(), 1);
		assertEquals(
				"[UPDATETIMEOUT] Null value on a 1 element timeout user list[val-test]...",
				uList.getTimeoutUserList().containsKey(user), true);
		// ////////////////////////////////////////////////////////////////////////////////
		uList.updateTimeout(other);
		assertEquals(
				"[UPDATETIMEOUT] Unknown value on a 1 element timeout user list[size-test]...",
				uList.getTimeoutUserList().size(), 1);
		assertEquals(
				"[UPDATETIMEOUT] Unknown value on a 1 element timeout user list[val-test]...",
				uList.getTimeoutUserList().containsKey(other), false);
		// ////////////////////////////////////////////////////////////////////////////////
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		uList.updateTimeout(user);
		Long updateTime = uList.getTimeoutUserList().get(user);
		assertEquals(
				"[UPDATETIMEOUT] Val 'user' on a 1 element timeout user list...",
				updateTime.equals(putTime), false);
		if (updateTime.compareTo(putTime) < 0)
			fail("[UPDATETIMEOUT] Erreur MAJ timeout...");
	}

	// //////////////////////////////////////////
	// REMOVE USER METHOD PART
	// __ Les differents tests unitaires permettent
	// de valider l'implementation des methodes
	// suivantes : removeProcess
	@Test
	public void removeUserTest() {
		uList = new UserList();

		// 1 user
		uList.getUserList().add(user);
		uList.getDiscussionUserList().add(discTest);
		uList.getTimeoutUserList().put(user, System.currentTimeMillis());
		uList.removeUser(null);
		assertEquals(
				"[REMOVEUSER-USERLIST] Null value to remove on a 1 element user list[size-test]...",
				uList.getUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-USERLIST] Null value to remove on a 1 element user list[val-test]...",
				uList.getUserList().contains(user), true);
		assertEquals(
				"[REMOVEUSER-DISCUSSIONLIST] Null value to remove on a 1 element user list[size-test]...",
				uList.getDiscussionUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-DISCUSSIONLIST] Null value to remove on a 1 element user list[val-test]...",
				uList.getDiscussionByUser(user).getUser(), user);
		assertEquals(
				"[REMOVEUSER-TIMEOUTLIST] Null value to remove on a 1 element user list[size-test]...",
				uList.getTimeoutUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-TIMEOUTLIST] Null value to remove on a 1 element user list[val-test]...",
				uList.getTimeoutUserList().containsKey(user), true);
		// ////////////////////////////////////////////////////////////////////////////////
		uList.removeUser(other);
		assertEquals(
				"[REMOVEUSER-USERLIST] Unknown value to remove on a 1 element user list[size-test]...",
				uList.getUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-USERLIST] Unknown value to remove on a 1 element user list[val-test]...",
				uList.getUserList().contains(user), true);
		assertEquals(
				"[REMOVEUSER-DISCUSSIONLIST] Unknown value to remove on a 1 element user list[size-test]...",
				uList.getDiscussionUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-DISCUSSIONLIST] Unknown value to remove on a 1 element user list[val-test]...",
				uList.getDiscussionByUser(user).getUser(), user);
		assertEquals(
				"[REMOVEUSER-TIMEOUTLIST] Unknown value to remove on a 1 element user list[size-test]...",
				uList.getTimeoutUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-TIMEOUTLIST] Unknown value to remove on a 1 element user list[val-test]...",
				uList.getTimeoutUserList().containsKey(user), true);
		// ////////////////////////////////////////////////////////////////////////////////
		uList.removeUser(user);
		assertEquals(
				"[REMOVEUSER-USERLIST] Val 'user' to remove on a 1 element user list[size-test]...",
				uList.getUserList().size(), 0);
		assertEquals(
				"[REMOVEUSER-USERLIST] Val 'user' to remove on a 1 element user list[val-test]...",
				uList.getUserList().contains(user), false);
		assertEquals(
				"[REMOVEUSER-DISCUSSIONLIST] Val 'user' to remove on a 1 element user list[size-test]...",
				uList.getDiscussionUserList().size(), 0);
		assertEquals(
				"[REMOVEUSER-DISCUSSIONLIST] Val 'user' to remove on a 1 element user list[val-test]...",
				uList.getDiscussionByUser(user), null);
		assertEquals(
				"[REMOVEUSER-TIMEOUTLIST] Val 'user' to remove on a 1 element user list[size-test]...",
				uList.getTimeoutUserList().size(), 0);
		assertEquals(
				"[REMOVEUSER-TIMEOUTLIST] Val 'user' to remove on a 1 element user list[val-test]...",
				uList.getTimeoutUserList().containsKey(user), false);

		// 2 users
		uList.getUserList().add(other);
		uList.getDiscussionUserList().add(otherDiscTest);
		uList.getTimeoutUserList().put(other, System.currentTimeMillis());
		uList.removeUser(user);
		assertEquals(
				"[REMOVEUSER-USERLIST] Val 'user' to remove on a 2 elements user list[size-test]...",
				uList.getUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-USERLIST] Val 'user' to remove on a 2 elements user list[val-test]...",
				uList.getUserList().contains(other), true);
		assertEquals(
				"[REMOVEUSER-DISCUSSIONLIST] Val 'user' to remove on a 2 elements user list[size-test]...",
				uList.getDiscussionUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-DISCUSSIONLIST] Val 'user' to remove on a 2 elements user list[val-test]...",
				uList.getDiscussionByUser(other).getUser(), other);
		assertEquals(
				"[REMOVEUSER-TIMEOUTLIST] Val 'user' to remove on a 2 elements user list[size-test]...",
				uList.getTimeoutUserList().size(), 1);
		assertEquals(
				"[REMOVEUSER-TIMEOUTLIST] Val 'user' to remove on a 2 elements user list[val-test]...",
				uList.getTimeoutUserList().containsKey(other), true);
	}
}
