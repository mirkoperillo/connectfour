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

/*la classe sviluppa l'albero di scelta delle mosse possibili da effettuare.
 Inoltre contiene le procedure che analizzano l'albero cos� costruito ritornando
 la mossa migliore fra tutte disponibili secondo l'algoritmo di min-max.
 */

public class EuristicTree {

	private EuristicNode start;
	private Grid configuration;
	public EuristicNode chosen;

	/* costruttore di classe inizializza le variabile della classe. */
	public EuristicTree() {
		start = null;
		configuration = null;
		chosen = new EuristicNode();
	}

	/*
	 * a questa build si puo passare come terzo parametro il livello di
	 * profondita al quale si vuole arrivare a costruire..livello che rispecchia
	 * il livello di difficolt� del gioco, player � il giocatore a cui tocca la
	 * prox mossa
	 */

	public void build(Grid conf, int player, int lev) {
		start = new EuristicNode();
		configuration = new Grid();
		configuration.setGrid(conf);
		EuristicNode temp = new EuristicNode();
		temp = start;
		int level = 0;
		while (temp != null) {
			boolean sonCreated = false;
			if (temp.getSon() == null && level < lev
					&& !configuration.isWon(temp.getColumn())) {
				int indexColumn = 0;
				do {
					int control = configuration.insertMove(indexColumn, player);
					if (control != -1) {
						EuristicNode newSon = new EuristicNode();
						newSon.setColumn(indexColumn);
						newSon.setFather(temp);
						temp.setSon(newSon);
						sonCreated = true;
						// torno in situazione precedente
						configuration.removeMove(indexColumn);
					}
					indexColumn++;
				} while (indexColumn <= 6);
			}
			if (sonCreated) {
				level++;
				temp = temp.getSon();
				// inserisci mossa del figlio in configuration
				configuration.insertMove(temp.getColumn(), player);
				player *= -1;
			} else {
				if (temp.getNext() != null) {
					// togli mossa corrente
					configuration.removeMove(temp.getColumn());
					temp = temp.getNext();
					// aggiungi quella del fratello
					configuration.insertMove(temp.getColumn(), player * (-1));
				} else {
					level--;
					// togli mossa corrente
					if (temp != start)
						configuration.removeMove(temp.getColumn());
					temp = temp.getFather();
					player *= -1;
				}
			}
		}
	}

	/*
	 * la procedura richiama al suo interno le procedure di pruning e min-max
	 * sull'albero di scelta e ritorna la mossa migliore da giocare per il
	 * giocatore player.
	 */
	public int play(int player) {
		choose(this.start, -9999, 9999, -player, 0);
		return chosen.getColumn();
	}

	/*
	 * procedura min-max: analizza l'albero di scelta settando il nodo choosen
	 * secondo il valore della miglior vantaggio di max trovato. Maggiori
	 * dettagli nella relazione. public int choose(EuristicNode tmp, int player,
	 * int level) { int retv; if(tmp!=this.start)
	 * configuration.insertMove(tmp.getColumn(), player); if(tmp.getSon()==null)
	 * { if((level%2)>0) retv = configuration.h(player); else retv =
	 * configuration.h(-player); if(configuration.isWon(tmp.getColumn()))
	 * if((level%2)>0) retv = 1000; else retv = -1000; } else { int comparev;
	 * EuristicNode tmp_son = new EuristicNode(); tmp_son = tmp.getSon();
	 * if((level%2)>0) retv = 9999; else retv = -9999; while(tmp_son!=null) {
	 * comparev = choose(tmp_son, -player, level+1); if((level%2)>0) {
	 * if(comparev < retv) { retv = comparev; } } else { if(comparev > retv) {
	 * retv = comparev; if(tmp==this.start) chosen = tmp_son; } } tmp_son =
	 * tmp_son.getNext(); } } if(tmp!=this.start)
	 * configuration.removeMove(tmp.getColumn()); return retv; }
	 * 
	 * 
	 * /*funzione pruning: la procedura visita l'albero di scelta e taglia i
	 * nodi non significativi nella successiva analisi effettuata dall'algoritmo
	 * min-max. Maggiori dettagli implementativi sono descritti nella relazione
	 * allegata.
	 */

	public int choose(EuristicNode tmp, int alpha, int beta, int player,
			int level) {
		int value;
		if (tmp != start)
			configuration.insertMove(tmp.getColumn(), player);
		if (tmp.getSon() != null) {
			EuristicNode tmp_son = new EuristicNode();
			tmp_son = tmp.getSon();
			while (tmp_son != null) {
				value = choose(tmp_son, alpha, beta, -player, level + 1);
				if ((level % 2) == 0) { // min
					if (value > alpha) {
						alpha = value;
						if (tmp == this.start)
							chosen = tmp_son;
					}
				} else { // MAX
					if (value < beta)
						beta = value;
				}
				if (alpha >= beta)
					break; // pruning
				else
					tmp_son = tmp_son.getNext();
			}
		}
		// FOGLIA
		else {
			if (configuration.isWon(tmp.getColumn())) {
				if ((level % 2) > 0)
					value = 1000;
				else
					value = -1000;
			} else {
				if ((level % 2) > 0)
					value = configuration.h(player);
				else
					value = configuration.h(-player);
			}
			configuration.removeMove(tmp.getColumn());
			return value;
		}
		if (tmp != start)
			configuration.removeMove(tmp.getColumn());
		if ((level % 2) > 0)
			return beta;
		else
			return alpha;
	}
}