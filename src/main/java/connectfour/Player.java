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

public class Player {

	/* variabili */
	/* nome del giocatore */
	private String name;
	/* giocatore umano o computer */
	private boolean human;

	/* metodi */
	/* costruttore */
	public Player() {
	}

	/* restituisce il nome del giocatore */
	public String getName() {
		return name;
	}

	/* dichiara se un giocatore � umano o computer */
	public boolean isHuman() {
		return human;
	}

	/* imposta il nome del giocatore */
	public void setName(String name) {
		this.name = new String(name);
	}

	/* imposta se un giocatore � umano o computer */
	public void setHuman(boolean human) {
		this.human = human;
	}
}