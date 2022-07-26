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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame_GameOverBox extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1312347435081825616L;
	/* variabili */
	JButton OkButton = new JButton();
	JLabel message;

	/* metodi */
	/* costruttore */
	public MainFrame_GameOverBox(Frame parent, String msg) {
		super(parent);
		/* il messaggio di game over */
		message = new JLabel(msg);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pack();
	}

	/* funzione di inizializzazione dei componenti */
	private void init() throws Exception {

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();

		FlowLayout flowLayout1 = new FlowLayout();
		FlowLayout flowLayout2 = new FlowLayout();
		FlowLayout flowLayout3 = new FlowLayout();

		setResizable(false);

		/* pulsante OK */
		panel1.setLayout(flowLayout1);
		panel1.setBackground(SystemColor.control);
		OkButton.setBackground(SystemColor.control);
		OkButton.setFont(new java.awt.Font("Dialog", 0, 11));
		OkButton.setText("Ok");
		OkButton.addActionListener(this);
		panel1.add(OkButton);

		/* messaggio */
		panel2.setLayout(flowLayout2);
		panel2.setBackground(SystemColor.control);
		message.setFont(new java.awt.Font("Dialog", 0, 11));
		panel2.add(message);

		/* emoticons */
		panel3.setLayout(flowLayout3);
		panel3.setBackground(SystemColor.control);
		JLabel icon = new JLabel();
		icon.setBorder(BorderFactory.createCompoundBorder());
		icon.setRequestFocusEnabled(false);
		if (message.getText().startsWith("Hai vinto"))
			icon.setIcon(new ImageIcon(getClass().getResource("/icons/angry.gif")));
		else if (message.getText().startsWith("Hai perso"))
			icon.setIcon(new ImageIcon(getClass().getResource("/icons/hot.gif")));
		else
			icon.setIcon(new ImageIcon(getClass().getResource("/icons/surprised.gif")));
		panel3.add(icon);

		/* assemblaggio */
		this.getContentPane().add(panel1, BorderLayout.SOUTH);
		this.getContentPane().add(panel2, BorderLayout.CENTER);
		this.getContentPane().add(panel3, BorderLayout.NORTH);
	}

	/*
	 * chiusura del dialog protected void processWindowEvent(WindowEvent e) { if
	 * (e.getID() == WindowEvent.WINDOW_CLOSING) { dispose(); }
	 * super.processWindowEvent(e); }
	 */

	/* chiusura del dialog da bottone */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == OkButton) {
			dispose();
		}
	}
}
