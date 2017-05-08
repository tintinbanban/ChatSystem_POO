package com.chat_system.model;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import communication.User;

/**
 * Classe contenant la liste des utilisateurs connectes au chatSystem et avec
 * qui on peut communiquer
 * 
 * @author Jacquouille
 * 
 */
public class UserList {
	// //////////////////////////////////////////
	// Attribut(s)
	private HashSet<User> userList;
	private HashSet<Discussion> discussionUserList;
	private HashMap<User, Long> timeoutUserList;

	// //////////////////////////////////////////
	// Constructeur(s)
	public UserList() {
		setUserList(new HashSet<User>());
		setDiscussionUserList(new HashSet<Discussion>());
		setTimeoutUserList(new HashMap<User, Long>());
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public HashSet<User> getUserList() {
		return userList;
	}

	public void setUserList(HashSet<User> userList) {
		this.userList = userList;
	}

	public HashSet<Discussion> getDiscussionUserList() {
		return discussionUserList;
	}

	public void setDiscussionUserList(HashSet<Discussion> discussionUserList) {
		this.discussionUserList = discussionUserList;
	}

	public HashMap<User, Long> getTimeoutUserList() {
		return timeoutUserList;
	}

	public void setTimeoutUserList(HashMap<User, Long> timeoutUserList) {
		this.timeoutUserList = timeoutUserList;
	}

	// //////////////////////////////////////////
	// Methode(s) specifique(s)
	public static String printUser(String username) {
		StringBuilder sB = new StringBuilder();
		sB.append("\n*____________________________________________________________________________________________*");
		sB.append("\nUN NOUVEL UTILISATEUR EST CONNECTE :	" + username);
		return sB.toString();
	}

	public String printUsers() {
		StringBuilder sB = new StringBuilder();
		sB.append("\n________________________________________________");
		sB.append("\nLISTE DES UTILISATEURS CONNECTES");
		sB.append("\n________________________________________________");
		for (User u : userList) {
			sB.append("\n-> ");
			sB.append(u.getPseudo());
			sB.append(u.getStatut());
		}
		return sB.toString();
	}

	// __ Methode qui verifie si un utilisateur donne en parametre
	// ____ appartient a la liste d'utilisateurs connectes en cours
	public User getUser(User user) {
		Iterator iterator = this.userList.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof User) {
				if (((User) o).equals(user)) {
					return (User) o;
				}
			}
		}
		return null;
	}

	// __ Methode qui retourne un utilisateur de la liste en fonction
	// ____ d'une adresse IP donnee en parametre
	public User getUserByIP(String ip) {
		Iterator iterator = this.userList.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof User) {
				if (((User) o).getIP().getHostAddress().equals(ip)) {
					return (User) o;
				}
			}
		}
		return null;
	}

	// __ Methode qui retourne un utilisateur de la liste en fonction
	// ____ d'une adresse IP et d'un port donnes en parametre : utilise
	// ____ lors de la reception d'un message en mode TCP
	public User getUserFromTCPCommunication(InetAddress ip, int port) {
		Iterator iterator = this.userList.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof User) {
				if (((User) o).getIP().getHostAddress()
						.equals(ip.getHostAddress())
						&& (((User) o).getPort() == port)) {
					return (User) o;
				}
			}
		}
		return null;
	}

	// __ Methode qui retourne un utilisateur de la liste en fonction
	// ____ de son pseudo donne en parametre
	public User getUserByPseudo(String pseudo) {
		Iterator iterator = this.userList.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof User) {
				if (((User) o).getPseudo().equals(pseudo)) {
					return (User) o;
				}
			}
		}
		return null;
	}

	// __ Methode qui cherche un utilisateur envoyant
	// ____ un message de deconnexion sur le chat
	public boolean userIsDeconnecting(User user) {
		if (user != null) {
			Iterator iterator = this.userList.iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o instanceof User) {
					User lUser = (User) o;
					if (lUser.getPseudo() != null) {
						if (user.getPseudo() != null) {
							if (lUser.getPseudo().equals(user.getPseudo())) {
								if (lUser.getIP() != null) {
									if (user.getIP() != null) {
										if (lUser.getIP().equals(user.getIP())) {
											if (lUser.getEtat() != null) {
												if (user.getEtat() != null) {
													if (lUser.getEtat() == User.typeConnect.CONNECTED) {
														if (user.getEtat() == User.typeConnect.DECONNECTED)
															return true;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	// __ Methode qui retourne un fil de discussion associe a un utilisateur
	// ____ appartenant a la liste des utilisateurs connectes
	public Discussion getDiscussionByUser(User user) {
		Iterator iterator = this.discussionUserList.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o != null) {
				if (o instanceof Discussion) {
					if (((Discussion) o).getUser() != null) {
						if (((Discussion) o).getUser().equals(user)) {
							return (Discussion) o;
						}
					}
				}
			}
		}
		return null;
	}

	// __ Methode qui se charge de mettre a jour la discussion avec un
	// utilisateur
	// ____ appelee pour ajouter les donnees envoyees par l'utilisateur local
	public void setLocalDiscussionByUser(String info, User user) {
		if (info != null && !info.isEmpty()) {
			Discussion discussion = getDiscussionByUser(user);
			if (discussion != null) {
				if (discussion.getUser().equals(user)) {
					discussion.completeLocalDiscussion(info);
				}
			}
		}
	}

	// __ Methode qui se charge de mettre a jour la discussion avec un
	// utilisateur
	// ____ appelee pour ajouter les donnees envoyees par l'utilisateur distant
	public void setDiscussionByUser(String info, User user) {
		if (info != null && !info.isEmpty()) {
			Discussion discussion = getDiscussionByUser(user);
			if (discussion != null) {
				if (discussion.getUser().equals(user)) {
					discussion.completeDiscussion(info);
				}
			}
		}
	}

	// __ Methode en charge de mettre a jour la liste des utilisateurs en cas de
	// ____ procedure de connexion d'un nouvel utilisateur : il sera ajoute et
	// un fil
	// ____ de discussion sera instancie
	public void addUser(User user) {
		if (user != null) {
			getUserList().add(user);
			getDiscussionUserList().add(new Discussion(user));
			getTimeoutUserList().put(user, System.currentTimeMillis());
		}
	}

	// __ Methode de mise a jour du timeout associe a un utilisateur
	public void updateTimeout(User user) {
		if (user != null) {
			if (getTimeoutUserList().containsKey(user)) {
				getTimeoutUserList().put(user, System.currentTimeMillis());
			}
		}
	}

	// __ Methode en charge de mettre a jour la liste des utilisateurs en cas de
	// ____ procedure de deconnexion : on supprime l'utilisateur et son fil
	// ____ de discussion associe
	public void removeUser(User user) {
		if (user != null) {
			Discussion discussion = getDiscussionByUser(user);
			if (discussion != null) {
				if (userIsDeconnecting(user)) {
					// -- [CAS 1] Message de deconnexion envoye
					user.setEtat(User.typeConnect.CONNECTED); // -- pour
																// permettre
																// la
																// suppression
					removeProcess(user, discussion);
				} else {
					// -- [CAS 2] Perte de connexion avec l'utilisateur
					removeProcess(user, discussion);
				}
			}
		}
	}

	private void removeProcess(User user, Discussion discussion) {
		userList.remove(user);
		discussionUserList.remove(discussion);
		timeoutUserList.remove(user);
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)
	// ...
}