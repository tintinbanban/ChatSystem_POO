package com.chat_system.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;

import com.chat_system.controller.Controller;
import com.chat_system.controller.Observateur;
import com.chat_system.model.Discussion;
import com.chat_system.model.UserList;
import communication.User;

/**
 * Classe gerant la partie interface graphique pour l'utilisateur
 * 
 * @author Jacquouille
 * 
 */
public class GUI extends JFrame implements ActionListener, KeyListener,
		Runnable, WindowListener, MouseListener, Observateur {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4422735517268985871L;
	// //////////////////////////////////////////
	// Attribut(s)
	private Controller controller;
	// -- Composants
	private JLabel loginL;
	private JLabel sendL;
	private JLabel receiveL;
	private JLabel userDiscussionL;
	private JTextField pseudoTF;
	protected JTextArea messSendTA;
	protected JTextArea messRecTA;
	private JButton sendB;
	private JButton connectB;
	protected BufferedWriter writer;
	protected BufferedReader reader;
	private JScrollPane scrollTASend;
	private JScrollPane scrollTARec;
	private JScrollPane scrollUl;
	private JLabel uListL;
	private JList<String> userList;
	// -- Autre(s) variable(s)
	private int RET_CONNECT = 1;
	private User destUser = null;
	private final String NOBODY = "nobody ...";

	// //////////////////////////////////////////
	// Constructeur(s)
	public GUI(Controller controller) {
		setController(controller);
		initLogin();
	}

	// //////////////////////////////////////////
	// Getteur(s) & Setteur(s)
	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	// //////////////////////////////////////////
	// Methode(s) specifique(s)
	// __ Fournit la fenetre de login au chat system
	private void initLogin() {
		// Instanciation des composants
		loginL = new JLabel("Pseudo : ");
		pseudoTF = new JTextField();
		pseudoTF.setColumns(10);
		connectB = new JButton("Connect");

		// Layout(s)
		FlowLayout fLayout = new FlowLayout(FlowLayout.CENTER, 25, 10);
		this.setLayout(fLayout);

		// Disposition des composants
		this.add(loginL);
		this.add(pseudoTF);
		this.add(connectB);

		// Ajout d'evenements
		connectB.addActionListener(this);
		this.addWindowListener(this);
		pseudoTF.addKeyListener(this);

		// Ajustement de la fenetre
		this.setPreferredSize(new Dimension(400, 90));
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		// Personnalisation de la fenetre
		this.setTitle("ChatSystem : Login form");
	}

	// __ Fournit la fenetre de chat pour dialoguer
	private void initChat() {
		// -- Variables locales
		int marg = 10;
		int wP1gl = 150;
		int wP2gl = 350;
		int h = 350;
		// -- Instanciation des composants
		sendL = new JLabel("message to send");
		receiveL = new JLabel("message to receive");
		sendB = new JButton("send");
		userDiscussionL = new JLabel();
		messSendTA = new JTextArea();
		messRecTA = new JTextArea();
		scrollTASend = new JScrollPane(messSendTA,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollTARec = new JScrollPane(messRecTA,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		userList = new JList<>();
		setEmptyList();
		scrollUl = new JScrollPane(userList);
		uListL = new JLabel("User List :");

		// -- Layout(s)
		getContentPane().removeAll();
		GridLayout g1Layout = new GridLayout(2, 1);
		GridLayout g2Layout = new GridLayout(2, 1);

		// -- Disposition des composants
		JPanel p1GLayout = new JPanel();
		p1GLayout.setLayout(g1Layout);
		JPanel p2GLayout = new JPanel();
		p2GLayout.setLayout(g2Layout);
		JPanel pFLayout = new JPanel();
		pFLayout.setLayout(new BoxLayout(pFLayout, BoxLayout.Y_AXIS));
		JPanel pBoxLayout = new JPanel();
		pBoxLayout.setLayout(new BoxLayout(pBoxLayout, BoxLayout.Y_AXIS));
		pBoxLayout.add(sendL);
		pBoxLayout.add(sendB);
		pBoxLayout.add(userDiscussionL);
		p1GLayout.add(pBoxLayout);
		p2GLayout.add(scrollTASend);
		p1GLayout.add(receiveL);
		p2GLayout.add(scrollTARec);
		pFLayout.add(uListL);
		pFLayout.add(scrollUl);
		this.setLayout(null);
		this.add(p1GLayout);
		this.add(p2GLayout);
		// -- Positionnement du panel contenant le GridLayout : partie chat
		p1GLayout.setBounds(marg, marg, wP1gl, h);
		// -- Positionnement du panel contenant le GridLayout : partie chat
		p2GLayout.setBounds(wP1gl + 2 * marg, marg, wP2gl, h);
		this.add(pFLayout);
		// -- Positionnement du panel contenant le BoxLayout : partie userList
		pFLayout.setBounds(wP1gl + wP2gl + 3 * marg, marg, 200, h);
		// -- Ajout d'evenements
		sendB.addActionListener(this);
		this.addWindowListener(this);
		messSendTA.setEditable(false);
		messSendTA.addKeyListener(this);
		messRecTA.setEditable(false);
		userList.addMouseListener(this);

		// -- Ajustement de la fenetre
		this.setPreferredSize(new Dimension(750, 400));
		this.setResizable(false);
		this.pack();

		// -- Personnalisation de la fenetre
		this.setTitle("ChatSystem : "
				+ this.getController().getLocalUser().getPseudo() + "@"
				+ this.getController().getLocalUser().getIP().getHostAddress()
				+ " [connected]");

		// -- Afficher la fenetre
		this.setVisible(true);
	}

	// __ Rafraichissement de la liste des utilisateurs connectes
	public void refreshUserList(UserList userList) {
		boolean flushBuffer = true;
		Object[] data = userList.getUserList().toArray();
		int i = 0;
		// -- tableau de chaines de caracteres qui va contenir les 'usernames'
		// -- (+statut) des utilisateurs du ChatSystem
		String[] sdata = new String[data.length];
		Discussion discussion = null;
		int indexSelected = this.userList.getSelectedIndex();
		User userSelected = null;

		while (i < data.length) {
			User user = (User) data[i];
			discussion = getController().getUserList()
					.getDiscussionByUser(user);
			if (discussion.getDiscussion().toString()
					.equals(this.messRecTA.getText())) {
				flushBuffer = false;
			}
			if (i == indexSelected) {
				userSelected = user;
			}
			sdata[i] = user.getPseudo() + "@" + user.getIP().getHostAddress();
			i++;
		}
		if (sdata.length == 0) {
			setEmptyList();
		} else {
			if (sdata != data) {
				this.userList.setListData(sdata);
				if (indexSelected != -1) { // -- NOBODY
					this.userList.setSelectedIndex(indexSelected);
					discussion = getController().getUserList()
							.getDiscussionByUser(userSelected);
					this.messRecTA.setText(discussion.getDiscussion()
							.toString());
				}
			}
		}
		if (flushBuffer)
			messRecTA.setText("");
	}

	private void loadDiscussion() {
	}

	// __ Methode qui etablit la connexion au ChatSystem :
	// ____ renvoie 0 si tout s'est bien passe,
	// ____ 1 sinon (en cas d'exceptions JAVA) -> impossibilite de se connecter
	private int connect() {
		if (!pseudoTF.getText().isEmpty()) {
			// -> appel 'performConnect' du controller
			this.getController().performConnect(pseudoTF.getText());

			return 0;
		} else if (pseudoTF.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Champ 'Pseudo' vide...",
					"ERREUR", JOptionPane.ERROR_MESSAGE);
		} 
		return 1;
	}

	private void sendMessage(User dest) {
		User user = null;
		try {
			this.getController().performMessage(messSendTA.getText().trim(),
					dest);
			if (this.destUser == null) {
				user = this.getController().getUserList().getUser(dest);
			} else {
				user = this.destUser;
			}
			// -- Mise a jour du fil de discussion
			UserList uList = this.getController().getUserList();
			Discussion discussion = uList.getDiscussionByUser(user);
			uList.setLocalDiscussionByUser(messSendTA.getText().trim(), user);
			this.messRecTA.setText(discussion.getDiscussion().toString());
			// -- Reinitialisation des valeurs
			reactivateSendTA();
			this.destUser = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setEmptyList() {
		String[] data = new String[1];
		data[0] = NOBODY;
		this.userList.setListData(data);
	}

	private void reactivateSendTA() {
		messSendTA.setText("");
		messSendTA.setEnabled(false);
		messSendTA.setEnabled(true);
	}

	private void prepareSending() {
		String pseudoCompare = null;
		String[] splitElement = null;
		if (!messSendTA.getText().isEmpty()
				&& !messSendTA.getText().matches("[\n|\t| ]*[\n|\t| ]*")) {
			// -- Si 'destUser' est null, on cherche quelle ligne est surlignee
			// ---- dans la JList
			if (destUser == null) {
				splitElement = userList.getSelectedValue().split("@");
				pseudoCompare = splitElement[0];

				if (pseudoCompare != null && !pseudoCompare.equals(NOBODY)) {
					destUser = this.getController().getUserList()
							.getUserByPseudo(pseudoCompare);
					if (destUser != null) {
						sendMessage(destUser);
					}
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"Aucun utilisateur n'a ete selectionne ou n'est present dans la liste...",
									"Envoi de message impossible",
									JOptionPane.ERROR_MESSAGE);
					reactivateSendTA();
				}
			} else {
				sendMessage(destUser);
			}
		} else
			JOptionPane.showMessageDialog(null, "Le texte est vide...",
					"Envoi de message impossible", JOptionPane.WARNING_MESSAGE);
	}

	private void printMessage(String info, User expUser) {
		// -- Recherche de l'utilisateur dans la JList
		ListModel model = userList.getModel();
		String pseudoCompare = null, ipCompare = null;
		String[] splitElement = null;
		for (int i = 0; i < model.getSize(); i++) {
			splitElement = model.getElementAt(i).toString().split("@");
			pseudoCompare = splitElement[0];
			ipCompare = splitElement[1];

			if (pseudoCompare.equals(expUser.getPseudo())
					&& ipCompare.equals(expUser.getIP())) {
				userList.setSelectedIndex(i);
			}
		}
		// -- Remplissage du buffer dedie a l'utilisateur qui a envoye le
		// -- message
		UserList uList = this.getController().getUserList();
		uList.setDiscussionByUser(info, expUser);
		this.messRecTA.setText(uList.getDiscussionByUser(expUser)
				.getDiscussion().toString());
		reactivateSendTA();
	}

	// //////////////////////////////////////////
	// Methode(s) redefinie(s)

	@Override
	// __ Pattern Observer : si l'objet 'UserList' est modifie, on rafraichit la
	// ____ liste des utilisateurs connectes au niveau de l'interface
	public void actualiser(com.chat_system.controller.Observable o,
			String method) {
		if (o instanceof Controller) {
			Controller c = (Controller) o;
			if (method.equals(Controller.method_name.processUser.name())) {
				this.refreshUserList(c.getUserList());
			}
			// -- Reception d'un message
			if (method.equals(Controller.method_name.processMessage.name())) {
				if (this.getController().getReceivedMessage() != null) {
					printMessage(this.getController().getReceivedMessage(),
							this.getController().getExpUser());
				}
			}
		}
	}

	// /////////////////////////////////////////////
	// METHODE RUN
	@Override
	public void run() {
		// -- Afficher la fenetre
		this.show();
		// -- On attend que la connexion soit etablie
		while (RET_CONNECT != 0) {
		}
		// -> affichage etat LOCAL USER
		System.out.println("--> LOCAL USER : "
				+ getController().getLocalUser().getPseudo());
		// -> une fois la connection etablie, on ajoute un observateur
		// pour mettre en place le pattern observer sur la liste d'utilisateurs
		// connectes
		this.getController().ajouterObservateur(this);
		// -- lancement de la deuxieme fenetre de l'application (fenetre de
		// chat)
		initChat();
	}

	// //////////////////////////////////////////
	// REDEFINITION WindowsListener
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	// __ Intervient au moment de la fermeture de l'application
	public void windowClosing(WindowEvent arg0) {
		if (controller.getsReceive() != null && controller.getsSend() != null) {
			if (!controller.getsReceive().getS().isClosed()
					&& !controller.getsSend().getS().isClosed()) {
				if (arg0.getWindow().getWidth() == 750) {
					try {
						// -- Procedure de deconnexion
						controller.performDisconnect();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		// -- On ferme l'application
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	// /////////////////////////////////////////////
	// REDEFINITION ActionListener
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// -- Si le bouton correspondant est le bouton 'Connect'
		if (arg0.getSource().equals(connectB))
			// ... on se connecte
			RET_CONNECT = connect();
		// -- Si le bouton correspondant est le bouton 'd'envoi' de message
		if (arg0.getSource().equals(sendB)) {
			prepareSending();
		}
	}

	// /////////////////////////////////////////////
	// REDEFINITIONS Keylistener
	@Override
	// __ Reaction a l'appui sur une touche du clavier
	public void keyPressed(KeyEvent arg0) {
		// -- Appui sur touche 'entree'
		if ((arg0.getKeyCode() == KeyEvent.VK_ENTER)) {
			// ... initLogin()
			if (arg0.getSource().equals(pseudoTF)) {
				// ... tentative de connexion
				RET_CONNECT = connect();
			}
			// ... initchat()
			if (arg0.getSource().equals(messSendTA)) {
				// ... envoi de message
				prepareSending();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	// /////////////////////////////////////////////
	// REDEFINITIONS MouseListener
	@Override
	// __ Reaction a l'action de la souris sur l'application
	public void mouseClicked(MouseEvent e) {
		// -- Action de la souris sur la JList 'userList'
		if (e.getSource().equals(userList)) {
			if (!userList.getSelectedValue().equals(NOBODY)) {
				// -- On cherche quel utilisateur correspond a la ligne
				// surlignee
				// de la JList
				int index = userList.locationToIndex(e.getPoint());
				int i = 0;
				Object[] uListToArray = controller.getUserList().getUserList()
						.toArray();
				while (i < uListToArray.length) {
					if (i == index) {
						if (uListToArray[i] instanceof User) {
							// -- Utilisateur trouve : on renseigne la valeur
							// de la
							// variable 'destUser'
							// pour envoyer un message en unicast si besoin
							destUser = (User) uListToArray[i];
						}
					}
					i++;
				}
				Discussion discussion = getController().getUserList()
						.getDiscussionByUser(destUser);
				this.messRecTA.setText(discussion.getDiscussion().toString());

				// -- Double clic de la souris
				if (e.getClickCount() == 2) {
					messSendTA.setEditable(true);
					messSendTA.grabFocus();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
