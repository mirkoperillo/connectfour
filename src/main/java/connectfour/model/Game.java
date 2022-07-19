package connectfour.model;

/*

 Copyright (C) 2003 Alessandro Zolet, Mirko Perillo
 Copyright (C) 2022 Mirko Perillo

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

public class Game {

	private Player player1;
	private Player player2;
	private Level level;

	private boolean singlePlayerGame;

	public Game() {
		player1 = new Player();
		player2 = new Player();
		level = Level.NORMAL;
	}

	public void newGame() {
		// gui.networkGame.enable_network();
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public boolean isSinglePlayer() {
		return singlePlayerGame;
	}

	public boolean isMultiPlayer() {
		return !singlePlayerGame;
	}

	/* run del thread */
	/*
	 * public void run() { gui.networkGame.enable_network(); }
	 */
}