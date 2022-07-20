package connectfour.logic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import connectfour.model.Configuration;
import connectfour.model.Level;
import connectfour.model.SavedGame;

public class CfgManager {

	/*
	 * caricamento delle variabili secondo le impostazioni predefinite del file di
	 * configurazione cfg presente nella cartella di installazione del gioco
	 */
	public Configuration loadCfg(File cfg) {
		boolean singlePlayerGame = true, usePlayer1 = true;
		String player1Name = "", player2Name = "", hostname = "";
		Level level = Level.NORMAL;

		String tmp = new String();
		String cfr = new String();
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(cfg)));
			do {
				tmp = r.readLine();
				tmp = tmp.trim();
				if (tmp.startsWith("begin") || tmp.startsWith("//"))
					;
				else {
					/* caso: giocatore singolo, viene inizializzato */
					cfr = "singlePlayer";
					if (tmp.startsWith(cfr)) {
						singlePlayerGame = true;
						if (tmp.endsWith("= 1;")) {
							usePlayer1 = true;
							// game.getPlayer1().setHuman(true);
							// game.getPlayer2().setHuman(false);
						}
						if (tmp.endsWith("= 2;")) {
							usePlayer1 = false;
							// game.getPlayer1().setHuman(false);
							// game.getPlayer2().setHuman(true);
						}
					}

					/* caso: multigiocatore */
					cfr = "multiPlayer";
					if (tmp.startsWith(cfr) && tmp.endsWith("= 1;")) {
						singlePlayerGame = false;
						// game.getPlayer1().setHuman(true);
						// game.getPlayer2().setHuman(true);
					}

					/*
					 * abilita il gioco in rete cfr = "network"; if(tmp.startsWith(cfr) &&
					 * tmp.endsWith("= 1;")) network = true; else if(tmp.startsWith(cfr) &&
					 * tmp.endsWith("= 0;")) network = false;
					 */

					/* imposta il nome del giocatore 1 */
					cfr = "name1";
					if (tmp.startsWith(cfr)) {
						tmp = tmp.substring(tmp.indexOf("'") + 1, tmp.lastIndexOf("'"));
						player1Name = tmp;
						// game.getPlayer1().setName(tmp);
					}

					/* imposta il nome del giocatore 2 */
					cfr = "name2";
					if (tmp.startsWith(cfr)) {
						tmp = tmp.substring(tmp.indexOf("'") + 1, tmp.lastIndexOf("'"));
						player2Name = tmp;
						// game.getPlayer2().setName(tmp);
					}

					/* configura IP */
					cfr = "ip";
					if (tmp.startsWith(cfr)) {
						tmp = tmp.substring(tmp.indexOf("'") + 1, tmp.lastIndexOf("'"));
						hostname = tmp;
						/* nome o ip dell'avversario */
						// networkGame.hostName = new String(tmp);
					}

					/* configura colori */

					/* altre opzioni */
					cfr = "level";
					if (tmp.startsWith(cfr)) {
						if (tmp.endsWith("= 4;"))
							// game.setLevel(Level.NORMAL);
							level = Level.NORMAL;
						else
							// game.setLevel(Level.HARD);
							level = Level.HARD;
					}

				}
			} while (!tmp.equals("end"));
			r.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return new Configuration(singlePlayerGame, usePlayer1, player1Name, player2Name, hostname, level);
	}

	/*
	 * caricamento della lista di mosse nell'array moves: l'array contiene i valori
	 * delle colonne giocate e -1 in eventuali mosse future non giocate ancora;
	 * l'indice marker segue l'ultima mossa valida
	 */
	public SavedGame loadMoveList(File saved) {
		int i = -1;
		int[] moves = new int[42];
		int marker = 0;
		try {
			DataInputStream reader = new DataInputStream(new FileInputStream(saved));
			do {
				i++;
				moves[i] = (reader.read() - 48);
			} while (moves[i] != -6 && i < 41);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int j = i; j < 42; j++)
			moves[j] = -1;
		for (marker = 0; moves[marker] != -1; marker++)
			;

		return new SavedGame(moves, marker);
	}
}
