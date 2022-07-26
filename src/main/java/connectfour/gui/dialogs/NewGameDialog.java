package connectfour.gui.dialogs;

/*

 Copyright (C) 2003 Alessandro Zolet, Mirko Perillo

 This file is part of connectfour.

 connectfour is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 FantaCalc is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with connectfour.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import connectfour.gui.MainFrame;

public class NewGameDialog extends JDialog {

	private static final long serialVersionUID = 611851780452800134L;
	/* variabili: il frame owner di questo dialog, le componenti ed i pulsanti */
	MainFrame ownerFrm;
	JRadioButton singlePlayer;
	JRadioButton multiPlayer;
	JRadioButton playerOne;
	JRadioButton playerTwo;
	JCheckBox network;
	JTextField enemyAddress;
	JButton okButton;
	JButton cancelButton;

	/* metodi */
	/* costruttore parametrico */
	public NewGameDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		ownerFrm = (MainFrame) frame;
		try {
			init();
			pack();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* funzione di inizializzazione */
	void init() throws Exception {

		/* due colonne di pannelli */
		GridLayout dlgLayout;
		JPanel lCol;
		GridLayout lColLayout;
		JPanel rCol;
		GridLayout rColLayout;

		dlgLayout = new GridLayout(1, 2);
		this.getContentPane().setLayout(dlgLayout);

		lColLayout = new GridLayout(6, 1);
		lCol = new JPanel(lColLayout);
		this.getContentPane().add(lCol, 0);
		rColLayout = new GridLayout(6, 1);
		rCol = new JPanel(rColLayout);
		this.getContentPane().add(rCol, 1);

		/* titolo */
		JLabel newGame = new JLabel("New Game: ");
		newGame.setFont(new java.awt.Font("Dialog", 0, 11));

		/*
		 * pulsante per la scelta del nuovo tipo di gioco: singolo giocatore, contro il
		 * computer, con la possibilit� di scegliere chi inizia il match;
		 * multigiocatore, con la possibilit� di scegliere la modalit� in rete
		 */
		singlePlayer = new JRadioButton();
		singlePlayer.setFont(new java.awt.Font("Dialog", 0, 11));
		singlePlayer.setText("Single Player, as:");
		if ((ownerFrm.game.getPlayer1().isHuman() && !ownerFrm.game.getPlayer2().isHuman())
				|| (!ownerFrm.game.getPlayer1().isHuman() && ownerFrm.game.getPlayer2().isHuman())) {
			singlePlayer.setSelected(true);
		}
		singlePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				singlePlayer_actionPerformed(e);
			}
		});

		/* per scegliere di giocare come giocatore primo */
		playerOne = new JRadioButton();
		playerOne.setFont(new java.awt.Font("Dialog", 0, 11));
		playerOne.setText("Player 1");
		if (!singlePlayer.isSelected())
			playerOne.setEnabled(false);
		if (ownerFrm.game.getPlayer1().isHuman())
			playerOne.setSelected(true);
		playerOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerOne_actionPerformed(e);
			}
		});

		/* per scegliere di giocare come secondo giocatore */
		playerTwo = new JRadioButton();
		playerTwo.setFont(new java.awt.Font("Dialog", 0, 11));
		playerTwo.setText("Player 2");
		if (!singlePlayer.isSelected())
			playerTwo.setEnabled(false);
		if (ownerFrm.game.getPlayer2().isHuman() && !playerOne.isSelected())
			playerTwo.setSelected(true);
		playerTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerTwo_actionPerformed(e);
			}
		});

		/* per scegliere multigiocatore: in rete o su un solo pc */
		multiPlayer = new JRadioButton();
		multiPlayer.setFont(new java.awt.Font("Dialog", 0, 11));
		multiPlayer.setText("Multi Player");
		if (ownerFrm.game.getPlayer1().isHuman() && ownerFrm.game.getPlayer2().isHuman()) {
			multiPlayer.setSelected(true);
		}
		multiPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				multiPlayer_actionPerformed(e);
			}
		});

		/* per selezionare la modalit� partita in rete */
		network = new JCheckBox();
		network.setFont(new java.awt.Font("Dialog", 0, 11));
		network.setText("Network, versus: ");
		if (!multiPlayer.isSelected())
			network.setEnabled(false);
		if (ownerFrm.game.isNetworkGame())
			network.setSelected(true);
		network.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				network_actionPerformed(e);
			}
		});

		/*
		 * il nome del computer collegato in rete con cui intendiamo fare una partita
		 */
		enemyAddress = new JTextField();
		enemyAddress.setFont(new java.awt.Font("Dialog", 0, 11));
		enemyAddress.setText(ownerFrm.game.getHostname());
		if (!network.isSelected())
			enemyAddress.setEnabled(false);

		/*
		 * il pulsante OK: settaggio del testo del pulsante, del font e dell'azione di
		 * pressione del pulsante
		 */
		okButton = new JButton("OK");
		okButton.setFont(new java.awt.Font("Dialog", 0, 11));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});

		/*
		 * il pulsante Cancel: settaggio del testo del pulsante, del font e della azione
		 * di pressione del pulsante
		 */
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(new java.awt.Font("Dialog", 0, 11));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});

		/* assemblaggio del dialog */
		lCol.add(newGame);
		JLabel empty01 = new JLabel();
		rCol.add(empty01);
		lCol.add(singlePlayer);
		JLabel empty02 = new JLabel();
		rCol.add(empty02);
		lCol.add(playerOne);
		rCol.add(playerTwo);
		lCol.add(multiPlayer);
		JLabel empty03 = new JLabel();
		rCol.add(empty03);
		lCol.add(network);
		rCol.add(enemyAddress);
		JPanel buttonPanel01 = new JPanel();
		FlowLayout buttonPanel01Layout = new FlowLayout();
		buttonPanel01.setLayout(buttonPanel01Layout);
		buttonPanel01.add(okButton/* , buttonPanel01Layout.CENTER */);
		lCol.add(buttonPanel01);
		JPanel buttonPanel02 = new JPanel(new FlowLayout());
		buttonPanel02.add(cancelButton);
		rCol.add(buttonPanel02);
	}

	/* selezione di Single Player */
	public void singlePlayer_actionPerformed(ActionEvent e) {
		singlePlayer.setSelected(true);
		playerOne.setEnabled(true);
		playerTwo.setEnabled(true);
		multiPlayer.setSelected(false);
		network.setSelected(false);
		network.setEnabled(false);
		enemyAddress.setEnabled(false);
	}

	/* selezione di Player One, ovvero il giocatore umano e' il primo */
	public void playerOne_actionPerformed(ActionEvent e) {
		playerOne.setSelected(true);
		playerTwo.setSelected(false);
	}

	/* selezione di Player One, ovvero il giocatore umano e' il secondo */
	public void playerTwo_actionPerformed(ActionEvent e) {
		playerOne.setSelected(false);
		playerTwo.setSelected(true);
	}

	/* selezione di Multi Player */
	public void multiPlayer_actionPerformed(ActionEvent e) {
		singlePlayer.setSelected(false);
		playerOne.setEnabled(false);
		playerTwo.setEnabled(false);
		multiPlayer.setSelected(true);
		network.setSelected(false);
		network.setEnabled(true);
	}

	/* selezione di gioco in rete */
	public void network_actionPerformed(ActionEvent e) {
		enemyAddress.setEnabled(true);
	}

	/*
	 * pressione del tasto annulla: non porta alcuna modifica e chiude il messaggio
	 * ritornando al frame chiamante
	 */
	public void cancelButton_actionPerformed(ActionEvent e) {
		ownerFrm.game.setBeginNewMatch(false);
		dispose();
	}

	/*
	 * pressione del tasto ok: modifica le variabili di gioco secondo le opzioni
	 * scelte
	 */
	public void okButton_actionPerformed(ActionEvent e) {

		/* scelta partita singolo giocatore contro il computer */
		if (singlePlayer.isSelected()) {
			if (playerOne.isSelected()) {
				ownerFrm.game.getPlayer1().setHuman(true);
				ownerFrm.game.getPlayer2().setHuman(false);
			} else {
				ownerFrm.game.getPlayer1().setHuman(false);
				ownerFrm.game.getPlayer2().setHuman(true);
			}
			ownerFrm.game.setNetworkGame(false);
		}

		/* scelta partita multigiocatore... */
		else {
			ownerFrm.game.getPlayer1().setHuman(true);
			ownerFrm.game.getPlayer2().setHuman(true);
			/* ...su un pc */
			if (!network.isSelected() || !network.isEnabled()) {
				ownerFrm.game.setNetworkGame(false);
			}
			/* ...in rete */
			else {
				ownerFrm.game.setNetworkGame(true);
			}
		}
		ownerFrm.game.setHostname(enemyAddress.getText());
		ownerFrm.game.setBeginNewMatch(true);
		dispose();
	}

}