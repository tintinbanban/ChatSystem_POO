package com.chat_system.model;

import java.text.DateFormat;
import java.util.Date;

import communication.User;

public class Discussion {
	// //////////////////////////////////////////
	// Attribut(s)
	private User user;
	private StringBuffer discussion;

	// //////////////////////////////////////////
	// Constructeur(s)
	public Discussion() {
		discussion = new StringBuffer();
	}

	public Discussion(User user) {
		this.user = user;
		discussion = new StringBuffer();
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public StringBuffer getDiscussion() {
		return discussion;
	}

	public void setDiscussion(StringBuffer discussion) {
		this.discussion = discussion;
	}

	// //////////////////////////////////////////
	// Méthode(s) spécifique(s)
	public void completeLocalDiscussion(String message) {
		if (message != null && !message.isEmpty()) {
			if (user != null) {
				// Format : 'vous: message'
				discussion.append("-->[MOI]:  ");
				discussion.append(message);
				discussion.append("\n");
			}
		}
	}

	public void completeDiscussion(String message) {
		if (message != null && !message.isEmpty()) {
			if (user != null) {
				// Format : 'user(heure): message'
				discussion.append(getUser().getPseudo());
				discussion.append("(");
				discussion.append(DateFormat.getTimeInstance(DateFormat.SHORT)
						.format(new Date()));
				discussion.append(")");
				discussion.append(":  ");
				discussion.append(message);
				discussion.append("\n");
			}
		}
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)
}
