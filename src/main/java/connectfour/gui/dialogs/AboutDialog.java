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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 6416164577449929939L;

	/* variabili */
	JButton button1 = new JButton();

	String product = "Connect Four";
	String version = "Software Version 2.0";
	String copyright = "Copyright (c) 2003-2022";
	String comments = "Zolet & Perillo";

	/* costruttore parametrico */
	public AboutDialog(Frame parent) {
		super(parent);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pack();
	}

	/* inizializzazione */
	private void init() throws Exception {

		/* crea gli stili di layout */
		BorderLayout borderLayout1 = new BorderLayout();
		BorderLayout borderLayout2 = new BorderLayout();

		FlowLayout flowLayout1 = new FlowLayout();

		GridLayout gridLayout1 = new GridLayout();

		/* crea etichette e pannelli */
		JLabel imageLabel = new JLabel();

		JLabel label1 = new JLabel();
		JLabel label2 = new JLabel();
		JLabel label3 = new JLabel();
		JLabel label4 = new JLabel();

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		JPanel insetsPanel1 = new JPanel();
		JPanel insetsPanel2 = new JPanel();
		JPanel insetsPanel3 = new JPanel();

		/* inizializza ed assembla i pannelli */
		// imageLabel.setIcon(new
		// ImageIcon(MainFrame_AboutBox.class.getResource("darkfaktory.gif")));
		// this.setTitle("About");
		setResizable(false);
		panel1.setLayout(borderLayout1);
		panel2.setLayout(borderLayout2);
		insetsPanel1.setLayout(flowLayout1);
		insetsPanel2.setLayout(flowLayout1);
		insetsPanel2.setBorder(BorderFactory.createEmptyBorder());
		gridLayout1.setRows(4);
		gridLayout1.setColumns(1);

		label1.setText(product);
		label1.setFont(new java.awt.Font("Dialog", 0, 11));

		label2.setText(version);
		label2.setFont(new java.awt.Font("Dialog", 0, 11));

		label3.setText(copyright);
		label3.setFont(new java.awt.Font("Dialog", 0, 11));

		label4.setText(comments);
		label4.setFont(new java.awt.Font("Dialog", 0, 11));
		insetsPanel3.setLayout(gridLayout1);
		insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));

		button1.setText("Ok");
		button1.addActionListener(this);
		button1.setFont(new java.awt.Font("Dialog", 0, 11));

		insetsPanel2.add(imageLabel, null);
		panel2.add(insetsPanel2, BorderLayout.WEST);
		this.getContentPane().add(panel1, null);
		insetsPanel3.add(label1, null);
		insetsPanel3.add(label2, null);
		insetsPanel3.add(label3, null);
		insetsPanel3.add(label4, null);
		panel2.add(insetsPanel3, BorderLayout.CENTER);
		insetsPanel1.add(button1, null);
		panel1.add(insetsPanel1, BorderLayout.SOUTH);
		panel1.add(panel2, BorderLayout.NORTH);
	}

	/*
	 * chiusura della finestra premendo la X equivale a premere ok protected void
	 * processWindowEvent(WindowEvent e) { if (e.getID() ==
	 * WindowEvent.WINDOW_CLOSING) { dispose(); } super.processWindowEvent(e); }/*
	 * 
	 * /*pulsante ok chiude il dialog
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1) {
			dispose();
		}
	}
}