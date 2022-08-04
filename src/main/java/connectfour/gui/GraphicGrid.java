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

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class GraphicGrid extends JPanel {

	private static final long serialVersionUID = -166299791939576787L;
	/* variabili */
	/* layout del pannello */
	GridLayout gridLayout;
	int columns = 7;
	int rows = 1;

	/* frame a cui il pannello appartiene */
	ApplicationWindow ownerFrm;

	public GraphicColumn column[] = new GraphicColumn[7];

	/* variabili per il controllo del tipo di quattro */
	public String mode;
	public int startC = -1;
	public int startR = -1;

	/* metodi */
	/* costruttore parametrico */
	public GraphicGrid(ApplicationWindow owner) {
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
		 * creazione delle colonne, e creazione autogestita per ciascuna delle righe
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
		int c = ownerFrm.game.removeLastMove();
		column[c].removeLast();
	}

	/* procedura di inserzione automatica di un gettone */
	public void loadGrid(int c) {
		column[c].loadPawn();
		if (!ownerFrm.game.isNetworkEnabled()) {
			// FIXME ownerFrm.menuMoveBack.setEnabled(true);
			ownerFrm.moveBack.setEnabled(true);
		}
	}

	/* procedura di animazione del quattro vincente */
	void markFour(int r, String gifName) {
		gifName = "/icons/" + gifName;
		/* orizzontale */
		if (mode.startsWith("hr")) {
			for (int i = startC; i < (startC + 4); i++) {
				column[i].render(r, gifName);
			}
		}
		/* verticale */
		if (mode.startsWith("vr")) {
			for (int i = r; i < (r + 4); i++) {
				column[startC].render(i, gifName);
			}
		}
		/* diagonale sinistro */
		if (mode.startsWith("dl")) {
			for (int i = startR, j = startC; i < (startR + 4) && j > (startC - 4); i++, j--) {
				column[j].render(i, gifName);
			}
		}
		/* diagonale destro */
		if (mode.startsWith("dr")) {
			for (int i = startR, j = startC; i < (startR + 4) && j < (startC + 4); i++, j++) {
				column[j].render(i, gifName);
			}
		}
		this.updateUI();
	}
}