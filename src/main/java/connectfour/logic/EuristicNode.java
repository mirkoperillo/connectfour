package connectfour.logic;

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

/*La classe EuristicNode rappresenta le mosse che il giocatore pc pu� effettuare
 durante una partita di Connect4.Le variabili pi� importanti di tale classe sono
 sicuramente la variabile intera column,che rappresenta il numero di colonna
 in cui verr� inserita suddetta mossa (numero che varia da 0 a 6), e la variabile
 intera h, che indica il grado di utilit� di tale mossa al fine di giungere
 alla vittoria del giocatore pc rispetto all�attuale configuarazione della
 griglia di gioco.
 Le altre tre variabili private di questa classe servono a localizzare
 la mossa all'interno dell'albero di scelta di cui � un elemento*/

public class EuristicNode {
	// intero che indica la colonna di inserimento
	private int column;
	// lista dei figli di tale mossa
	private SonList sons;
	// variabile che tiene traccia del fratello all'interno dell'albero
	private EuristicNode next;
	// variabile che tiene traccia del padre della mossa all'interno dell'albero
	private EuristicNode father;

	/*
	 * costruttore della classe EuristicNode.In particolare tale costruttore
	 * inizializza tutte le variabili ad un valore nullo, ad eccezione
	 * dell'oggetto SonList che viene allocato.
	 */
	public EuristicNode() {
		column = -1;
		sons = new SonList();
		next = null;
		father = null;
	}

	/* routine che assegna il valore passato in parametro alla variabile column */
	public void setColumn(int c) {
		column = c;
	}

	/*
	 * routine che assegna l'oggetto passato in parametro come fratello dell'
	 * EuristicNode
	 */
	public void setNext(EuristicNode next) {
		this.next = new EuristicNode();
		this.next = next;
	}

	/*
	 * routine che assegna l'oggetto passato in parametro come padre dell'
	 * EuristicNode
	 */
	public void setFather(EuristicNode father) {
		this.father = new EuristicNode();
		this.father = father;
	}

	/*
	 * routine che aggiunge l'oggetto passato in parametro alla lista dei figli
	 * di tale EuristicNode sfruttando la routine insertSon presente negli
	 * oggetti SonList
	 */
	public void setSon(EuristicNode newSon) {
		sons.insertSon(newSon);
	}

	/* routine che ritorna il valore di column */
	public int getColumn() {
		return column;
	}

	/*
	 * routine che ritorna il fratello di un EuristicNode, ossia il suo
	 * successivo nella lista di figli del padre
	 */
	public EuristicNode getNext() {
		return next;
	}

	/* routine che ritorna il padre di un EuristicNode */
	public EuristicNode getFather() {
		return father;
	}

	/*
	 * routine che ritorna il primo figlio di un oggetto EuristicNode,come in
	 * parti colare si pu� notare dal corpo della routine, tale funzione sfrutta
	 * il metodo getFirst() presente in ogni oggetto SonList
	 */
	public EuristicNode getSon() {
		return sons.getFirst();
	}
}