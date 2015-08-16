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

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GraphicGrid extends JPanel {

	/* variabili */
	/* layout del pannello */
	GridLayout gridLayout;
	int columns = 7;
	int rows = 1;

	/* frame a cui il pannello appartiene */
	MainFrame ownerFrm;

	GraphicColumn column[] = new GraphicColumn[7];

	/* variabili per il controllo del tipo di quattro */
	public String mode;
	public int startC = -1;
	public int startR = -1;

	/* metodi */
	/* costruttore parametrico */
	public GraphicGrid(MainFrame owner) {
		ownerFrm = owner;
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* procedura di inizializzazione */
	void init() throws Exception {

		mode = new String();

		gridLayout = new GridLayout();
		gridLayout.setRows(rows);
		gridLayout.setColumns(columns);
		this.setLayout(gridLayout);

		/*
		 * creazione delle colonne, e creazione autogestita per ciascuna delle
		 * righe
		 */
		Dimension gd = getSize();
		for (int i = 0; i < columns; i++) {
			column[i] = new GraphicColumn(i, this);
			column[i].setPreferredSize(new Dimension(gd.width / 7, gd.height));
			this.add(column[i], i);
		}
	}

	/* procedura di svuotamento della griglia */
	void cleanGrid() {
		for (int i = 0; i < columns; i++)
			column[i].cleanColumn();
	}

	/* procedura di rimozione di un gettone */
	void removeLast() {
		/* nella lista delle mosse non viene eliminato */
		ownerFrm.launcherApp.marker--;
		int c = ownerFrm.launcherApp.moves[ownerFrm.launcherApp.marker];
		column[c].removeLast();
	}

	/* procedura di inserzione automatica di un gettone */
	void loadGrid(int c) {
		column[c].loadPawn();
		if (!ownerFrm.launcherApp.netenabled) {
			ownerFrm.menuMoveBack.setEnabled(true);
			ownerFrm.moveBack.setEnabled(true);
		}
	}

	/* procedura di animazione del quattro vincente */
	void markFour(int r, String gifName) {
		int counter = 0;
		/* orizzontale */
		if (mode.startsWith("hr")) {
			for (int i = startC; i < (startC + 4); i++) {
				column[i].row[r].removeAll();
				column[i].imageLabel[r].removeAll();
				column[i].imageLabel[r].setIcon(new ImageIcon(GraphicGrid.class
						.getResource(gifName)));
				column[i].row[r].add(column[i].imageLabel[r], null);
			}
		}
		/* verticale */
		if (mode.startsWith("vr")) {
			for (int i = r; i < (r + 4); i++) {
				column[startC].row[i].removeAll();
				column[startC].imageLabel[i].removeAll();
				column[startC].imageLabel[i].setIcon(new ImageIcon(
						GraphicGrid.class.getResource(gifName)));
				column[startC].row[i].add(column[startC].imageLabel[i], null);
			}
		}
		/* diagonale sinistro */
		if (mode.startsWith("dl")) {
			for (int i = startR, j = startC; i < (startR + 4)
					&& j > (startC - 4); i++, j--) {
				column[j].row[i].removeAll();
				column[j].imageLabel[i].removeAll();
				column[j].imageLabel[i].setIcon(new ImageIcon(GraphicGrid.class
						.getResource(gifName)));
				column[j].row[i].add(column[j].imageLabel[i], null);
			}
		}
		/* diagonale destro */
		if (mode.startsWith("dr")) {
			for (int i = startR, j = startC; i < (startR + 4)
					&& j < (startC + 4); i++, j++) {
				column[j].row[i].removeAll();
				column[j].imageLabel[i].removeAll();
				column[j].imageLabel[i].setIcon(new ImageIcon(GraphicGrid.class
						.getResource(gifName)));
				column[j].row[i].add(column[j].imageLabel[i], null);
			}
		}
		this.updateUI();
	}
}