package connectfour;

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

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraphicColumn extends JPanel {

	/* variabili */
	/* identificatore della colonna */
	int id;

	// flag
	boolean flag;

	/* griglia di appartenenza */
	GraphicGrid ownerGrd;

	/* layout del pannello */
	GridLayout gridLayout;
	/* ogni singola colonna ha sei righe */
	int columns = 1;
	int rows = 6;

	/* pannelli ed etichette su cui disegnare i gettoni o gli spazi vuoti */
	JPanel row[] = new JPanel[6];
	JLabel imageLabel[] = new JLabel[6];

	/* Mouse Listener */
	Mouse mouse = new Mouse(this);

	/* metodi */
	/* costruttore parametrico */
	public GraphicColumn(int index, GraphicGrid owner) {
		id = index;
		// semaphore = 1 - semaphore;
		ownerGrd = owner;

		// flag=true;

		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* procedura di inizializzazione */
	void init() throws Exception {

		/* impostazione del layout */
		gridLayout = new GridLayout();
		gridLayout.setRows(rows);
		gridLayout.setColumns(columns);
		this.setLayout(gridLayout);

		/* costruzione di una griglia vuota */
		for (int i = 0; i < rows; i++) {
			row[i] = new JPanel();
			row[i].setBackground(new Color(70, 70, 70));
			imageLabel[i] = new JLabel();
			imageLabel[i].setIcon(new ImageIcon(GraphicColumn.class
					.getResource("empty.gif")));
			row[i].add(imageLabel[i], null);
			this.add(row[i], i);
		}

		/* costruzione del mouse listener che permette l'inserzione dei gettoni */
		this.addMouseListener(mouse);
	}

	/* gestione del risultato di un click sulla colonna */
	public void clickOnColumn(MouseEvent e) {

		/*
		 * protezione dall'evento 'mouseClicked' durante una partita in rete
		 * mentre il turno � dell'avversario oppure dopo avere inserito un
		 * gettone
		 */
		if (ownerGrd.ownerFrm.launcherApp.netenabled)
			if (ownerGrd.ownerFrm.launcherApp.myColumn != -1)
				return;
		if (ownerGrd.ownerFrm.launcherApp.netenabled
				&& !ownerGrd.ownerFrm.launcherApp.neturn)
			return;

		/*
		 * il click ha effetto soltanto su configurazioni di partite non
		 * concluse oppure su quelle concluse ma da cui poi sono state rimossi
		 * uno o pi� gettoni
		 */
		if (!ownerGrd.ownerFrm.gameOver || ownerGrd.ownerFrm.removing) {
			int r = ownerGrd.ownerFrm.launcherApp.gameGrid.insertMove(id,
					ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer);
			if (r < 0) {
				ownerGrd.ownerFrm.statusBar.setText(" Column " + (id + 1)
						+ " is full...");
			} else {
				ownerGrd.ownerFrm.removing = false;

				/*
				 * se l'inserzione avviene con successo non vengono mostrati
				 * messaggi di errore o altri
				 */
				ownerGrd.ownerFrm.statusBar.setText(" ");

				/* aggiornamento della lista delle mosse a inserzione avvenuta */
				ownerGrd.ownerFrm.launcherApp.moves[ownerGrd.ownerFrm.launcherApp.marker] = id;
				ownerGrd.ownerFrm.launcherApp.marker++;
				for (int i = ownerGrd.ownerFrm.launcherApp.marker; i <= 41; i++) {
					if (ownerGrd.ownerFrm.launcherApp.moves[i] != -1)
						ownerGrd.ownerFrm.launcherApp.moves[i] = -1;
				}

				if (!ownerGrd.ownerFrm.launcherApp.netenabled) {
					/*
					 * dopo aver inserito un gettone automaticamente possiamo
					 * anche toglierlo
					 */
					ownerGrd.ownerFrm.menuMoveBack.setEnabled(true);
					ownerGrd.ownerFrm.moveBack.setEnabled(true);
					/*
					 * dopo aver inserito un gettone automaticamente non �
					 * possibile annullare un Undo
					 */
					ownerGrd.ownerFrm.menuMoveForward.setEnabled(false);
					ownerGrd.ownerFrm.moveForward.setEnabled(false);
					/*
					 * la 42esima � l'ultima mossa possibile: non ha pi� senso
					 * accettare i suggerimenti
					 */
					if (ownerGrd.ownerFrm.launcherApp.marker >= 42) {
						ownerGrd.ownerFrm.playHint.setEnabled(false);
						ownerGrd.ownerFrm.menuMovePlay.setEnabled(false);
					}
				}
				/* aggiornamento della grafica: inserzione virtuale del gettone */
				// row[r].removeAll();
				// imageLabel[r].removeAll();
				if (ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer > 0)
					imageLabel[r].setIcon(new ImageIcon(GraphicColumn.class
							.getResource("yellow.gif")));
				else
					imageLabel[r].setIcon(new ImageIcon(GraphicColumn.class
							.getResource("red.gif")));
				// row[r].add(imageLabel[r], null);
				// row[r].updateUI();
				row[r].paint(row[r].getGraphics());

				/* controllo della vincita */
				if (ownerGrd.ownerFrm.launcherApp.gameGrid.isWon(id)) {

					ownerGrd.ownerFrm.gameOver = true;
					ownerGrd.ownerFrm.removing = false;

					/*
					 * evidenziamo il quattro con una breve animazione che
					 * termina non appena si d� conferma che la partita �
					 * conclusa
					 */
					if (ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer > 0)
						ownerGrd.markFour(r, "yellow_spin.gif");
					else
						ownerGrd.markFour(r, "red_spin.gif");

					/* creazione del messaggio di vittoria (o sconfitta) */
					String message = new String();
					int winner = ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer;

					/* se la partita � vinta dal giocatore 1... */
					if (winner > 0)
						message = "Hai vinto "
								+ ownerGrd.ownerFrm.launcherApp.one.getName()
								+ "!";
					/* se la partita � vinta dal giocatore 2... */
					else
						message = "Hai vinto "
								+ ownerGrd.ownerFrm.launcherApp.two.getName()
								+ "!";

					/* invio del messaggio di fine partita */
					ownerGrd.ownerFrm.gameOver(message);

					/* animazione */
					if (ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer > 0)
						ownerGrd.markFour(r, "yellow.gif");
					else
						ownerGrd.markFour(r, "red.gif");
					/* reinizializzazione variabili per l'animazione */
					ownerGrd.mode = new String("");
					ownerGrd.startC = -1;
					ownerGrd.startR = -1;

					/*
					 * non appena il gioco in rete finisce, riparte una nuova
					 * partita
					 */
					if (ownerGrd.ownerFrm.launcherApp.netenabled) {
						ownerGrd.ownerFrm.menuQuickNew_actionPerformed(null);
					}
				}

				/* conclusione in parit� */
				else if (ownerGrd.ownerFrm.launcherApp.gameGrid.isFull()) {
					ownerGrd.ownerFrm.gameOver = true;
					ownerGrd.ownerFrm.removing = false;
					String message = "Partita pari...";
					ownerGrd.ownerFrm.gameOver(message);
					if (ownerGrd.ownerFrm.launcherApp.netenabled) {
						ownerGrd.ownerFrm.menuQuickNew_actionPerformed(null);
					}
				}

				/*
				 * la colonna giocata passa da -1 a 'id' (ossia questa colonna),
				 * nel caso giocassimo in rete
				 */
				if (ownerGrd.ownerFrm.launcherApp.netenabled)
					ownerGrd.ownerFrm.launcherApp.myColumn = id;

				/* cambia il turno del giocatore */
				ownerGrd.ownerFrm.launcherApp.gameGrid.changeTurn();

				/*
				 * se tocca al computer, questo gioca automaticamente la sua
				 * mossa
				 */

				if (ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer > 0
						&& !ownerGrd.ownerFrm.launcherApp.one.isHuman())
					ownerGrd.ownerFrm.menuMovePlay_actionPerformed(null);
				else if (ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer < 0
						&& !ownerGrd.ownerFrm.launcherApp.two.isHuman())
					ownerGrd.ownerFrm.menuMovePlay_actionPerformed(null);

				synchronized (this.ownerGrd.ownerFrm) {
					for (int i = 0; i < 7; i++)
						this.ownerGrd.column[i].flag = false;
					try {
						this.ownerGrd.ownerFrm.notify();
					} catch (Exception exc) {
					}
				}
			}
		}
	}

	/*
	 * cancella l'ultima mossa fatta; essa � comunque recuperabile con un Redo,
	 * poich� dalla lista delle mosse essa non viene cancellata fin quando non
	 * viene sovrascritta
	 */
	void removeLast() {
		int r = ownerGrd.ownerFrm.launcherApp.gameGrid.numberRow(id);
		/* cancellazione fisica della mossa dalla griglia */
		ownerGrd.ownerFrm.launcherApp.gameGrid.removeMove(id);
		/* cambio giocatore a quello che ha rimosso */
		ownerGrd.ownerFrm.launcherApp.gameGrid.changeTurn();

		/* eliminazione del gettone virtuale, si sostituisce con un vuoto */
		row[r].removeAll();
		imageLabel[r].removeAll();
		imageLabel[r].setIcon(new ImageIcon(GraphicColumn.class
				.getResource("empty.gif")));
		row[r].add(imageLabel[r]);
		this.updateUI();
	}

	/* svuota la colonna (ossia la riempie di buchi) */
	void cleanColumn() {
		for (int i = 0; i < rows; i++) {
			row[i].removeAll();
			imageLabel[i].removeAll();
			imageLabel[i].setIcon(new ImageIcon(GraphicColumn.class
					.getResource("empty.gif")));
			row[i].add(imageLabel[i]);
		}
	}

	/*
	 * carica il gettone senza l'evento click sulla colonna; questa funzione �
	 * utile per caricare i gettoni delle mosse del computer, dei suggerimenti,
	 * delle partite salvate non concluse; � analoga alla clickOnColumn: per il
	 * significato delle righe affini, vedere sopra.
	 */
	void loadPawn() {
		if (!ownerGrd.ownerFrm.gameOver || ownerGrd.ownerFrm.removing) {
			int r = ownerGrd.ownerFrm.launcherApp.gameGrid.insertMove(id,
					ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer);
			// row[r].removeAll();
			// imageLabel[r].removeAll();
			if (ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer > 0)
				imageLabel[r].setIcon(new ImageIcon(GraphicColumn.class
						.getResource("yellow.gif")));
			else
				imageLabel[r].setIcon(new ImageIcon(GraphicColumn.class
						.getResource("red.gif")));
			// row[r].add(imageLabel[r], null);
			// row[r].updateUI();
			row[r].paint(row[r].getGraphics());

			/* controllo vincita */
			if (ownerGrd.ownerFrm.launcherApp.gameGrid.isWon(id)) {
				ownerGrd.ownerFrm.gameOver = true;
				ownerGrd.ownerFrm.removing = false;
				if (ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer > 0)
					ownerGrd.markFour(r, "yellow_spin.gif");
				else
					ownerGrd.markFour(r, "red_spin.gif");

				int winner = ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer;
				String message = new String();
				/* se vince il giocatore 1 */
				if (winner > 0) {
					if (ownerGrd.ownerFrm.launcherApp.one.isHuman())
						message = "Hai vinto, "
								+ ownerGrd.ownerFrm.launcherApp.one.getName()
								+ "!";
					else
						message = "Hai perso...";
				}
				/* se vince il giocatore 2 */
				else {
					if (ownerGrd.ownerFrm.launcherApp.two.isHuman())
						message = "Hai vinto, "
								+ ownerGrd.ownerFrm.launcherApp.two.getName()
								+ "!";
					else
						message = "Hai perso...";
				}
				/*
				 * quando invece il gettone viene caricato dalla rete (sempre
				 * persa)
				 */
				if (ownerGrd.ownerFrm.launcherApp.netenabled)
					message = "Hai perso...";
				ownerGrd.ownerFrm.gameOver(message);

				if (ownerGrd.ownerFrm.launcherApp.gameGrid.currentPlayer > 0)
					ownerGrd.markFour(r, "yellow.gif");
				else
					ownerGrd.markFour(r, "red.gif");
				ownerGrd.mode = new String("");
				ownerGrd.startC = -1;
				ownerGrd.startR = -1;

				if (ownerGrd.ownerFrm.launcherApp.netenabled) {
					ownerGrd.ownerFrm.menuQuickNew_actionPerformed(null);
				}
			}
			/* controllo pareggio */
			else if (ownerGrd.ownerFrm.launcherApp.gameGrid.isFull()) {
				ownerGrd.ownerFrm.gameOver = true;
				ownerGrd.ownerFrm.removing = false;
				String message = "Partita pari...";
				ownerGrd.ownerFrm.gameOver(message);
				if (ownerGrd.ownerFrm.launcherApp.netenabled)
					ownerGrd.ownerFrm.menuQuickNew_actionPerformed(null);
			}
		}
		ownerGrd.ownerFrm.launcherApp.gameGrid.changeTurn();
	}
}