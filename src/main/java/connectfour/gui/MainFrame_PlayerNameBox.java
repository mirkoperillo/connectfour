package connectfour.gui;

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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame_PlayerNameBox extends JDialog {

	private static final long serialVersionUID = -1732349663926972731L;
	/* variabili */
	/* frame owner */
	MainFrame ownerFrm;
	/* layout del dialog */
	GridLayout gridLayout = new GridLayout(3, 2);
	JLabel name1 = new JLabel();
	JLabel name2 = new JLabel();
	/* campi di testo per scrivere i nomi dei giocatori */
	JTextField player1name = new JTextField(12);
	JTextField player2name = new JTextField(12);
	/* pulsante ok di conferma e cancel di annullamento */
	JPanel okButtonPanel = new JPanel(new FlowLayout());
	JPanel cancelButtonPanel = new JPanel(new FlowLayout());
	JButton okButton = new JButton();
	JButton cancelButton = new JButton();

	/* metodi */
	/* costruttore parametrico */
	public MainFrame_PlayerNameBox(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		ownerFrm = (MainFrame) frame;
		try {
			init();
			pack();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* inizializzazione */
	void init() throws Exception {

		getContentPane().setLayout(gridLayout);

		/* costruzione dei componenti */
		name1.setFont(new java.awt.Font("Dialog", 0, 11));
		name1.setText("Player 1: ");

		name2.setFont(new java.awt.Font("Dialog", 0, 11));
		name2.setText("Player 2: ");

		player1name.setFont(new java.awt.Font("Dialog", 0, 11));
		player1name.setText(ownerFrm.game.getPlayer1().getName());

		player2name.setFont(new java.awt.Font("Dialog", 0, 11));
		player2name.setText(ownerFrm.game.getPlayer2().getName());

		okButton.setFont(new java.awt.Font("Dialog", 0, 11));
		okButton.setText("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});

		cancelButton.setFont(new java.awt.Font("Dialog", 0, 11));
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});

		/* assemblaggio del dialog */
		getContentPane().add(name1);
		getContentPane().add(player1name);
		getContentPane().add(name2);
		getContentPane().add(player2name);
		okButtonPanel.add(okButton);
		getContentPane().add(okButtonPanel);
		cancelButtonPanel.add(cancelButton);
		getContentPane().add(cancelButtonPanel);

		this.pack();
	}

	/* metodi */
	/*
	 * pressione del pulsante OK, salva i nomi desiderati fino a 20 caratteri, il
	 * resto viene eventualmente ignorato
	 */
	public void okButton_actionPerformed(ActionEvent e) {
		if (player1name.getText() == null || player1name.getText().trim() == "")
			return;
		try {
			/* lettura del campo di testo */
			String name = new String(player1name.getText());
			/*
			 * se il nome � pi� lungo di 20 caratteri dal 21esimo vengono ignorati
			 */
			if (name.length() > 20)
				name = name.substring(0, 20);
			/* altrimenti vengono contati fino alla lunghezza del nome */
			else
				name = name.substring(0, name.length());
			/* nuovo nome impostato, analogo per il giocatore 2 */
			ownerFrm.game.getPlayer1().setName(name);
			name = player2name.getText();
			if (name.length() > 20)
				name = name.substring(0, 20);
			else
				name = name.substring(0, name.length());
			ownerFrm.game.getPlayer2().setName(name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.dispose();
	}

	/* pressione del pulsante Cancel */
	public void cancelButton_actionPerformed(ActionEvent e) {
		this.dispose();
	}
}