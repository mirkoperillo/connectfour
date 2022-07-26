package connectfour.model;

import connectfour.gui.Gui;

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

/**
 * classe che descrive e gestisce le configurazioni della griglia di gioco su
 * cui si basa l'aggiornamento della grigglia grafica. La classe gestisce
 * inoltre mediante i suoi metodi tutte le operazioni possibili sulla griglia di
 * gioco, dall'inserimento alla rimozione, tutti i controlli relativi alle
 * regole di gioco.
 */

public class Grid {

	// riferimento all' oggetto CFApp che ha costruito la griglia
	Gui launcherApp;

	/*
	 * giocatore giallo. L'inizializzazione a 1 significa che per regola il gioco
	 * inizia sempre con una mossa del giocatore giallo, identificato per l'appunto
	 * dal valore 1.
	 */
	public int currentPlayer = 1;

	// griglia di gioco reale, matrice di interi 6x7 che tiene traccia dello
	// stato
	// dei propri slots.
	private int grid[][];

	public Grid(Gui launcher) {
		launcherApp = launcher;
		this.grid = new int[6][7];
		init();
	}

	// costruttore non parametrico di Grid. Alloca lo spazio alla matrice
	// bidimensionale e lancia il metodo privato init che la inizializza
	public Grid() {
		this.grid = new int[6][7];
		init();
	}

	// routine di inizializzazione di una griglia.
	// tutte le entrate della matrice vengono inizializzate a 0
	public void init() {
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++)
				grid[i][j] = 0;
	}

	/*
	 * routine che esegue l'inserimento di una mossa nella griglia di gioco. Ha due
	 * parametri la colonna in cui inserire la pedina e il giocatore che ha
	 * effettuato tale mossa. La routine ritorna un'intero che corrisponde alla riga
	 * in cui � stata inserita la pedina nella colonna prescelta.Se tale colonna
	 * risulta piena la routine ritorna un valore non valido(-1) e l'inserimento non
	 * viene eseguito
	 */

	public int insertMove(int column, int playerSign) {
		if (isFull(column))
			return -1;
		int j;
		for (j = 5; grid[j][column] != 0; j--)
			;
		grid[j][column] = playerSign;
		return j;
	}

	/*
	 * la routine effettua la rimozione dell'ultima pedina inserita nella colonna
	 * presa come parametro.In particolare se la colonna prescelta risulta vuota non
	 * viene effettuata alcuna modifica alla griglia, altrimenti si setta la
	 * relativa entrata della matrice al valore 0, ossia slot libero.
	 */
	public void removeMove(int column) {
		if (!isEmpty(column)) {
			if (isFull(column))
				grid[0][column] = 0;
			else {
				int j;
				for (j = 5; grid[j][column] != 0; j--)
					;
				grid[j + 1][column] = 0;
			}
		}
	}

	/*
	 * la routine ritorna il numero di riga corrispondente all'ultima pedina
	 * inserita nella colonna presa in parametro
	 */
	public int numberRow(int column) {
		if (isFull(column))
			return 0;
		else {
			int j;
			for (j = 5; grid[j][column] != 0; j--)
				;
			return j + 1;
		}
	}

	/* routine che setta la configurazione di una griglia */
	public void setGrid(Grid origine) {
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++)
				this.grid[i][j] = origine.grid[i][j];
	}

	/*
	 * la routine verifica la colonna presa in esame � vuota,ritornando in questo
	 * caso valore true, oppure se tale colonna contiene gi� della pedina,
	 * ritornando allore false
	 */
	public boolean isEmpty(int column) {
		int counter = 0;
		for (int j = 5; j >= 0; j--)
			if (grid[j][column] == 0)
				counter++;
		if (counter == 6)
			return true;
		else
			return false;
	}

	/*
	 * la routine verifica se la colonna presa come parametro � piena oppure se non
	 * lo � ritornando i valori boolenani corrispondenti.
	 */
	public boolean isFull(int column) {
		if (grid[0][column] != 0)
			return true;
		return false;
	}

	/*
	 * routine che verifica se all'interno della griglia di gioco tutte le colonne
	 * sono piene.
	 */
	public boolean isFull() {
		for (int i = 0; i < 7; i++)
			if (!isFull(i))
				return false;
		return true;
	}

	/*
	 * La procedura verifica se ci sono configurazioni vincenti nella griglia
	 * appartenenti ad un giocatore.Se viene trovato un 4 viene ritornato true e
	 * viene tenuta traccia del tipo di 4 fatto per poterlo animare in seguito.
	 */

	public Winner isWon(int column) {
		if (column == -1)
			return new Winner(false, null, -1, -1);
		int counter = 0;
		int row = this.numberRow(column);
		int playerSign = this.grid[row][column];
		// controls victory hr
		for (int j = 0; j < 7; j++) {
			if (grid[row][j] == playerSign)
				counter++;
			else
				counter = 0;
			if (counter >= 4) {
				return new Winner(true, "hr", j - 3, -1);
			}
		}

		// controls victory vr
		counter = 0;
		for (int i = 0; i < 6; i++) {
			if (grid[i][column] == playerSign)
				counter++;
			else
				counter = 0;
			if (counter >= 4) {
				return new Winner(true, "vr", column, -1);
			}
		}

		// controls victory dl
		counter = 0;
		int k = row, l = column;
		while ((k < 5) && (l > 0)) {
			k = k + 1;
			l = l - 1;
		}
		for (int i = k, j = l; (i >= 0) && (j <= 6); i--, j++) {
			if (grid[i][j] == playerSign)
				counter++;
			else
				counter = 0;
			if (counter >= 4) {
				return new Winner(true, "dl", j, i);
			}
		}

		// controls victory dr
		counter = 0;
		k = row;
		l = column;
		while ((k < 5) && (l < 6)) {
			k = k + 1;
			l = l + 1;
		}
		for (int i = k, j = l; (i >= 0) && (j >= 0); i--, j--) {
			if (grid[i][j] == playerSign)
				counter++;
			else
				counter = 0;
			if (counter >= 4) {
				return new Winner(true, "dr", j, i);
			}
		}
		return new Winner(false, null, -1, -1);
	}

	public static record Winner(boolean isWin, String mode, int startC, int startR) {

	}

	/*
	 * la routine cambia il turno di gioco dei giocatori sempilicemente
	 * moltiplicando il valore per -1.Infatti per semplicit� i giocatori assumono i
	 * valori 1, per quanto riguarda il primo giocatore, e -1 per il secondo
	 */
	public void changeTurn() {
		currentPlayer *= -1;
	}

	/* funzione di calcolo dei possibili quattro per un giocatore */
	public int getFours(int player) {
		int i;
		int j;
		int h;
		int k;
		int res;
		res = 0;

		for (i = 0; i < 6; i++) {
			for (j = 0; j < 4; j++) {
				for (h = j; h < j + 4; h++)
					if (grid[i][h] == (-player))
						break;
				if (h - j == 4)
					res++;
			}
		}

		for (j = 0; j < 7; j++) {
			for (i = 0; i < 3; i++) {
				for (h = i; h < i + 4; h++)
					if (grid[h][j] == (-player))
						break;
				if (h - i == 4)
					res++;
			}
		}

		for (i = 3; i < 6; i++) {
			for (j = 0; j < 4; j++) {
				for (h = i, k = j; k < j + 4; h--, k++)
					if (grid[h][k] == (-player))
						break;
				if (i - h == 4)
					res++;
			}
		}

		for (i = 0; i < 3; i++) {
			for (j = 0; j < 4; j++) {
				for (h = i, k = j; k < j + 4; h++, k++)
					if (grid[h][k] == (-player))
						break;
				if (h - i == 4)
					res++;
			}
		}

		return res;
	}

	/* funzione di calcolo euristico */
	public int h(int player) {
		// return (getFours(this.currentPlayer) -
		// getFours(-this.currentPlayer));
		return (getFours(player) - getFours(-player));
	}
}