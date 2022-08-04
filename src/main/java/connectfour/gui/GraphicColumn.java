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

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import connectfour.model.Grid.Winner;

public class GraphicColumn extends JPanel {

	private static final long serialVersionUID = -4012663836485100299L;

	/* variabili */
	/* identificatore della colonna */
	int id;

	// flag
	public boolean flag;

	/* griglia di appartenenza */
	GraphicGrid ownerGrd;

	/* layout del pannello */
	private GridLayout gridLayout;
	/* ogni singola colonna ha sei righe */
	private int columns = 1;
	private int rows = 6;

	/* pannelli ed etichette su cui disegnare i gettoni o gli spazi vuoti */
	private JPanel row[] = new JPanel[6];
	private JLabel imageLabel[] = new JLabel[6];

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
			imageLabel[i].setIcon(new ImageIcon(getClass().getResource("/icons/empty.gif")));
			row[i].add(imageLabel[i], null);
			this.add(row[i], i);
		}

		/* costruzione del mouse listener che permette l'inserzione dei gettoni */
		this.addMouseListener(mouse);
	}

	/* gestione del risultato di un click sulla colonna */
	public void clickOnColumn(MouseEvent e) {

		/*
		 * protezione dall'evento 'mouseClicked' durante una partita in rete mentre il
		 * turno � dell'avversario oppure dopo avere inserito un gettone
		 */
		// FIXME
//		if (ownerGrd.ownerFrm.launcherApp.game.isNetworkEnabled())
//			if (ownerGrd.ownerFrm.launcherApp.networkGame.myColumn != -1)
//				return;
//		if (ownerGrd.ownerFrm.launcherApp.game.isNetworkEnabled() && !ownerGrd.ownerFrm.launcherApp.networkGame.neturn)
//			return;

		/*
		 * il click ha effetto soltanto su configurazioni di partite non concluse oppure
		 * su quelle concluse ma da cui poi sono state rimossi uno o pi� gettoni
		 */
		if (!ownerGrd.ownerFrm.gameOver || ownerGrd.ownerFrm.removing) {
			int r = ownerGrd.ownerFrm.game.gameGrid.insertMove(id, ownerGrd.ownerFrm.game.gameGrid.currentPlayer);
			if (r < 0) {
				ownerGrd.ownerFrm.statusBarMsg("Column " + (id + 1) + " is full...");
			} else {
				ownerGrd.ownerFrm.removing = false;

				/*
				 * se l'inserzione avviene con successo non vengono mostrati messaggi di errore
				 * o altri
				 */
				ownerGrd.ownerFrm.resetStatusBar();

				/* aggiornamento della lista delle mosse a inserzione avvenuta */
				ownerGrd.ownerFrm.game.nextMove(id);

				if (!ownerGrd.ownerFrm.game.isNetworkEnabled()) {
					/*
					 * dopo aver inserito un gettone automaticamente possiamo anche toglierlo
					 */
					// FIXME ownerGrd.ownerFrm.menuMoveBack.setEnabled(true);
					ownerGrd.ownerFrm.moveBack.setEnabled(true);
					/*
					 * dopo aver inserito un gettone automaticamente non � possibile annullare un
					 * Undo
					 */
					// FIXME ownerGrd.ownerFrm.menuMoveForward.setEnabled(false);
					ownerGrd.ownerFrm.moveForward.setEnabled(false);
					/*
					 * la 42esima � l'ultima mossa possibile: non ha pi� senso accettare i
					 * suggerimenti
					 */
					if (ownerGrd.ownerFrm.game.noMoreMoves()) {
						ownerGrd.ownerFrm.playHint.setEnabled(false);
						// FIXME ownerGrd.ownerFrm.menuMovePlay.setEnabled(false);
					}
				}
				/* aggiornamento della grafica: inserzione virtuale del gettone */
				// row[r].removeAll();
				// imageLabel[r].removeAll();
				if (ownerGrd.ownerFrm.game.gameGrid.currentPlayer > 0)
					imageLabel[r].setIcon(new ImageIcon(getClass().getResource("/icons/yellow.gif")));
				else
					imageLabel[r].setIcon(new ImageIcon(getClass().getResource("/icons/red.gif")));
				// row[r].add(imageLabel[r], null);
				// row[r].updateUI();
				row[r].paint(row[r].getGraphics());

				/* controllo della vincita */
				Winner win = ownerGrd.ownerFrm.game.gameGrid.isWon(id);
				if (win.isWin()) {
					ownerGrd.mode = win.mode();
					ownerGrd.startC = win.startC();
					ownerGrd.startR = win.startR();
					ownerGrd.ownerFrm.gameOver = true;
					ownerGrd.ownerFrm.removing = false;

					/*
					 * evidenziamo il quattro con una breve animazione che termina non appena si d�
					 * conferma che la partita � conclusa
					 */
					if (ownerGrd.ownerFrm.game.gameGrid.currentPlayer > 0)
						ownerGrd.markFour(r, "yellow_spin.gif");
					else
						ownerGrd.markFour(r, "red_spin.gif");

					/* creazione del messaggio di vittoria (o sconfitta) */
					String message = new String();
					int winner = ownerGrd.ownerFrm.game.gameGrid.currentPlayer;

					/* se la partita � vinta dal giocatore 1... */
					if (winner > 0)
						message = "Hai vinto " + ownerGrd.ownerFrm.game.getPlayer1().getName() + "!";
					/* se la partita � vinta dal giocatore 2... */
					else
						message = "Hai vinto " + ownerGrd.ownerFrm.game.getPlayer2().getName() + "!";

					/* invio del messaggio di fine partita */
					ownerGrd.ownerFrm.gameOver(message);

					/* animazione */
					if (ownerGrd.ownerFrm.game.gameGrid.currentPlayer > 0)
						ownerGrd.markFour(r, "yellow.gif");
					else
						ownerGrd.markFour(r, "red.gif");
					/* reinizializzazione variabili per l'animazione */
					ownerGrd.mode = new String("");
					ownerGrd.startC = -1;
					ownerGrd.startR = -1;

					/*
					 * non appena il gioco in rete finisce, riparte una nuova partita
					 */
					if (ownerGrd.ownerFrm.game.isNetworkEnabled()) {
						ownerGrd.ownerFrm.menuQuickNew_actionPerformed(null);
					}
				}

				/* conclusione in parit� */
				else if (ownerGrd.ownerFrm.game.gameGrid.isFull()) {
					ownerGrd.ownerFrm.gameOver = true;
					ownerGrd.ownerFrm.removing = false;
					String message = "Partita pari...";
					ownerGrd.ownerFrm.gameOver(message);
					if (ownerGrd.ownerFrm.game.isNetworkEnabled()) {
						ownerGrd.ownerFrm.menuQuickNew_actionPerformed(null);
					}
				}

				/*
				 * la colonna giocata passa da -1 a 'id' (ossia questa colonna), nel caso
				 * giocassimo in rete
				 */
				// FIXME
//				if (ownerGrd.ownerFrm.launcherApp.game.isNetworkEnabled())
//					ownerGrd.ownerFrm.launcherApp.networkGame.myColumn = id;

				/* cambia il turno del giocatore */
				ownerGrd.ownerFrm.game.gameGrid.changeTurn();

				/*
				 * se tocca al computer, questo gioca automaticamente la sua mossa
				 */

				if (ownerGrd.ownerFrm.game.gameGrid.currentPlayer > 0 && !ownerGrd.ownerFrm.game.getPlayer1().isHuman())
					ownerGrd.ownerFrm.play();
				else if (ownerGrd.ownerFrm.game.gameGrid.currentPlayer < 0
						&& !ownerGrd.ownerFrm.game.getPlayer2().isHuman())
					ownerGrd.ownerFrm.play();

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
	 * poich� dalla lista delle mosse essa non viene cancellata fin quando non viene
	 * sovrascritta
	 */
	void removeLast() {
		int r = ownerGrd.ownerFrm.game.gameGrid.numberRow(id);
		/* cancellazione fisica della mossa dalla griglia */
		ownerGrd.ownerFrm.game.gameGrid.removeMove(id);
		/* cambio giocatore a quello che ha rimosso */
		ownerGrd.ownerFrm.game.gameGrid.changeTurn();

		/* eliminazione del gettone virtuale, si sostituisce con un vuoto */
		row[r].removeAll();
		imageLabel[r].removeAll();
		imageLabel[r].setIcon(new ImageIcon(getClass().getResource("/icons/empty.gif")));
		row[r].add(imageLabel[r]);
		this.updateUI();
	}

	/* svuota la colonna (ossia la riempie di buchi) */
	void cleanColumn() {
		for (int i = 0; i < rows; i++) {
			row[i].removeAll();
			imageLabel[i].removeAll();
			imageLabel[i].setIcon(new ImageIcon(getClass().getResource("/icons/empty.gif")));
			row[i].add(imageLabel[i]);
		}
	}

	/*
	 * carica il gettone senza l'evento click sulla colonna; questa funzione � utile
	 * per caricare i gettoni delle mosse del computer, dei suggerimenti, delle
	 * partite salvate non concluse; � analoga alla clickOnColumn: per il
	 * significato delle righe affini, vedere sopra.
	 */
	void loadPawn() {
		if (!ownerGrd.ownerFrm.gameOver || ownerGrd.ownerFrm.removing) {
			int r = ownerGrd.ownerFrm.game.gameGrid.insertMove(id, ownerGrd.ownerFrm.game.gameGrid.currentPlayer);
			// row[r].removeAll();
			// imageLabel[r].removeAll();
			if (ownerGrd.ownerFrm.game.gameGrid.currentPlayer > 0)
				imageLabel[r].setIcon(new ImageIcon(getClass().getResource("/icons/yellow.gif")));
			else
				imageLabel[r].setIcon(new ImageIcon(getClass().getResource("/icons/red.gif")));
			// row[r].add(imageLabel[r], null);
			// row[r].updateUI();
			row[r].paint(row[r].getGraphics());

			/* controllo vincita */
			Winner win = ownerGrd.ownerFrm.game.gameGrid.isWon(id);
			if (win.isWin()) {
				ownerGrd.mode = win.mode();
				ownerGrd.startC = win.startC();
				ownerGrd.startR = win.startR();
				ownerGrd.ownerFrm.gameOver = true;
				ownerGrd.ownerFrm.removing = false;
				if (ownerGrd.ownerFrm.game.gameGrid.currentPlayer > 0)
					ownerGrd.markFour(r, "yellow_spin.gif");
				else
					ownerGrd.markFour(r, "red_spin.gif");

				int winner = ownerGrd.ownerFrm.game.gameGrid.currentPlayer;
				String message = new String();
				/* se vince il giocatore 1 */
				if (winner > 0) {
					if (ownerGrd.ownerFrm.game.getPlayer1().isHuman())
						message = "Hai vinto, " + ownerGrd.ownerFrm.game.getPlayer1().getName() + "!";
					else
						message = "Hai perso...";
				}
				/* se vince il giocatore 2 */
				else {
					if (ownerGrd.ownerFrm.game.getPlayer2().isHuman())
						message = "Hai vinto, " + ownerGrd.ownerFrm.game.getPlayer2().getName() + "!";
					else
						message = "Hai perso...";
				}
				/*
				 * quando invece il gettone viene caricato dalla rete (sempre persa)
				 */
				if (ownerGrd.ownerFrm.game.isNetworkEnabled())
					message = "Hai perso...";
				ownerGrd.ownerFrm.gameOver(message);

				if (ownerGrd.ownerFrm.game.gameGrid.currentPlayer > 0)
					ownerGrd.markFour(r, "yellow.gif");
				else
					ownerGrd.markFour(r, "red.gif");
				ownerGrd.mode = new String("");
				ownerGrd.startC = -1;
				ownerGrd.startR = -1;

				if (ownerGrd.ownerFrm.game.isNetworkEnabled()) {
					ownerGrd.ownerFrm.menuQuickNew_actionPerformed(null);
				}
			}
			/* controllo pareggio */
			else if (ownerGrd.ownerFrm.game.gameGrid.isFull()) {
				ownerGrd.ownerFrm.gameOver = true;
				ownerGrd.ownerFrm.removing = false;
				String message = "Partita pari...";
				ownerGrd.ownerFrm.gameOver(message);
				if (ownerGrd.ownerFrm.game.isNetworkEnabled())
					ownerGrd.ownerFrm.menuQuickNew_actionPerformed(null);
			}
		}
		ownerGrd.ownerFrm.game.gameGrid.changeTurn();
	}

	public void render(int index, String gifName) {
		row[index].removeAll();
		imageLabel[index].removeAll();
		imageLabel[index].setIcon(new ImageIcon(getClass().getResource(gifName)));
		row[index].add(imageLabel[index], null);
	}
}