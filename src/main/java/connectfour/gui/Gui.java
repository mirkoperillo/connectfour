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
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.UIManager;

import connectfour.logic.NetworkGame;
import connectfour.model.Grid;
import connectfour.model.Player;

public class Gui {

	/* variabili per la gestione del frame */
	boolean packFrame = false;
	public MainFrame frame;

	/* variabili per la gestione di gioco */
	Grid gameGrid;

	/* array con lista delle mosse */
	public int moves[];

	/* indice che definisce l'ultima mossa valida */
	public int marker;

	/* giocatore che inzia la partita */
	static Player one;

	/* secondo giocatore */
	static Player two;

	/* livello di gioco del computer */
	static int level;

	boolean beginNewMatch = true;

	/* variabili per la gestione di rete */

	public NetworkGame networkGame;

	public Gui() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		networkGame = new NetworkGame(this);
		one = new Player();
		two = new Player();

		/* path di installazione del gioco */
		String gamePath = new File("").getAbsolutePath();

		/* caricamento delle impostazioni e assegnazione delle variabili */
		File gameCfg = new File(gamePath + "/cfg.txt");
		loadCfg(gameCfg);

		/* caricamento della lista di mosse in un array */
		moves = new int[42];
		marker = 0;
		File saveCfg = new File(gamePath + "/save.txt");
		/*
		 * in rete non viene considerata l'ultima partita giocata; altrimenti viene
		 * caricata - se precedentemente non era stata conclusa
		 */
		if (saveCfg.exists() && !networkGame.network)
			loadMoveList(saveCfg);
		else
			for (int i = 0; i < moves.length; i++)
				moves[i] = -1;

		/* griglia fisica di gioco */
		gameGrid = new Grid(this);
		// gameGrid.init();

		/*
		 * frame principale: a sua volta richiama il costruttore del frame e dei suoi
		 * componenti grafici
		 */
		frame = new MainFrame(this);

		/* valida il frame di grandezza preselezionata e pack del frame */
		if (packFrame) {
			frame.pack();
		} else {
			frame.validate();
		}

		/* centra il frame rispetto allo schermo */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		/* il frame non � ridimensionabile */
		frame.setResizable(false);

		/* il frame � sempre visualizzato */
		frame.setVisible(true);
	}

	/* __________________________________________________________________________ */
	/*
	 * _______________assegnazione variabili loadCfg_____________________________
	 */

	/*
	 * caricamento delle variabili secondo le impostazioni predefinite del file di
	 * configurazione cfg presente nella cartella di installazione del gioco
	 */
	public void loadCfg(File cfg) {
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
						if (tmp.endsWith("= 1;")) {
							one.setHuman(true);
							two.setHuman(false);
						}
						if (tmp.endsWith("= 2;")) {
							one.setHuman(false);
							two.setHuman(true);
						}
					}

					/* caso: multigiocatore */
					cfr = "multiPlayer";
					if (tmp.startsWith(cfr) && tmp.endsWith("= 1;")) {
						one.setHuman(true);
						two.setHuman(true);
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
						one.setName(tmp);
					}

					/* imposta il nome del giocatore 2 */
					cfr = "name2";
					if (tmp.startsWith(cfr)) {
						tmp = tmp.substring(tmp.indexOf("'") + 1, tmp.lastIndexOf("'"));
						two.setName(tmp);
					}

					/* configura IP */
					cfr = "ip";
					if (tmp.startsWith(cfr)) {
						tmp = tmp.substring(tmp.indexOf("'") + 1, tmp.lastIndexOf("'"));
						/* nome o ip dell'avversario */
						networkGame.hostName = new String(tmp);
					}

					/* configura colori */

					/* altre opzioni */
					cfr = "level";
					if (tmp.startsWith(cfr)) {
						if (tmp.endsWith("= 4;"))
							level = 4;
						else
							level = 6;
					}

				}
			} while (!tmp.equals("end"));
			r.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/*
	 * caricamento della lista di mosse nell'array moves: l'array contiene i valori
	 * delle colonne giocate e -1 in eventuali mosse future non giocate ancora;
	 * l'indice marker segue l'ultima mossa valida
	 */
	public void loadMoveList(File saved) {
		int i = -1;
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
	}

}