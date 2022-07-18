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

/* classe che gestisce i figli dei NodiEuristici tramite una lista
 come appunto il nome suggerisce*/

public class SonList {
	private EuristicNode first;

	/*
	 * routine di aggiunta nodo a ListaFigli.In particolare il nodo passato come
	 * parametro viene aggiunto in coda all lista di figli
	 */

	public void insertSon(EuristicNode f) {
		if (first == null) {
			first = new EuristicNode();
			first = f;
		} else {
			EuristicNode temp = new EuristicNode();
			temp = first;
			while (temp.getNext() != null)
				temp = temp.getNext();
			temp.setNext(f);
		}
	}

	public SonList() {
		first = null;
	}

	// ritorna l'indirizzo del primo elemento della lista
	public EuristicNode getFirst() {
		return first;
	}
}