package connectfour.model;

import java.util.Arrays;

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
	private int[] moves;
	private int marker;

	private String hostname;
	private boolean networkGame;

	private boolean netenabled;

	public Grid gameGrid;
	private boolean beginNewMatch = true;

	public Game() {
		player1 = new Player();
		player1.setName("");
		player2 = new Player();
		player2.setName("");
		level = Level.NORMAL;
		singlePlayerGame = true;
		moves = new int[42];
		marker = 0;

		gameGrid = new Grid();
	}

	public Game(Configuration cfg) {
		player1 = new Player();
		player1.setName(cfg.player1Name());
		player2 = new Player();
		player2.setName(cfg.player2Name());
		level = cfg.level();
		singlePlayerGame = cfg.singlePlayerGame();
		hostname = cfg.hostname();

		moves = new int[42];
		marker = 0;

		gameGrid = new Grid();
	}

	// FIXME needed ??
	public void newGame() {
		// gui.networkGame.enable_network();
	}

	public void newNetworkGame() {
		netenabled = true;
		// start server networkManager.startServer()
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

	public void loadSavedGame(SavedGame savedGame) {
		moves = savedGame.moves();
		marker = savedGame.marker();

	}

	public void nextMove(int column) {
		moves[marker] = column;
		marker++;
		// FIXME needed ??
		for (int i = marker; i <= 41; i++) {
			if (moves[i] != -1)
				moves[i] = -1;
		}

	}

	public int removeLastMove() {
		marker--;
		return moves[marker];
	}

	public boolean noMoreMoves() {
		return marker >= 42;
	}

	public int lastMove() {
		int move = moves[marker];
		marker++;
		return move;
	}

	public void reset() {
		Arrays.fill(moves, -1);
		marker = 0;
	}

	public boolean isSinglePlayerGame() {
		return singlePlayerGame;
	}

	public int[] getMoves() {
		return moves;
	}

	public int getMarker() {
		return marker;
	}

	public boolean isGameOver() {
		// FIXME assign correct value, when is the game over ?
		return false;
	}

	public boolean isNetworkGame() {
		return networkGame;
	}

	public boolean isNetworkEnabled() {
		return netenabled;
	}

	public void setNetworkGame(boolean networkGame) {
		this.networkGame = networkGame;
	}

	public String getHostname() {
		return hostname;
	}

	public void disableNetwork() {
		netenabled = false;
		// networkManager.disableNetwork()
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public boolean isBeginNewMatch() {
		return beginNewMatch;
	}

	public void setBeginNewMatch(boolean beginNewMatch) {
		this.beginNewMatch = beginNewMatch;
	}

	/* run del thread */
	/*
	 * public void run() { gui.networkGame.enable_network(); }
	 */
}