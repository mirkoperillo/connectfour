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
import java.io.File;

import javax.swing.UIManager;

import connectfour.logic.CfgManager;
import connectfour.logic.NetworkGame;
import connectfour.model.Configuration;
import connectfour.model.Game;
import connectfour.model.Grid;
import connectfour.model.SavedGame;

public class Gui {

	/* variabili per la gestione del frame */
	boolean packFrame = false;
	public MainFrame frame;

	/* variabili per la gestione di gioco */
	Grid gameGrid;

	public Game game;

	boolean beginNewMatch = true;

	/* variabili per la gestione di rete */

	public NetworkGame networkGame;

	private CfgManager cfgManager;

	public Gui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		cfgManager = new CfgManager();

		/* path di installazione del gioco */
		String gamePath = new File("").getAbsolutePath();

		/* caricamento delle impostazioni e assegnazione delle variabili */
		File gameCfg = new File(gamePath + "/cfg.txt");
		Configuration cfg = cfgManager.loadCfg(gameCfg);
		this.game = new Game(cfg);

		File saveCfg = new File(gamePath + "/save.txt");
		/*
		 * in rete non viene considerata l'ultima partita giocata; altrimenti viene
		 * caricata - se precedentemente non era stata conclusa
		 */
		if (saveCfg.exists() && !game.isNetworkGame()) {
			SavedGame savedGame = cfgManager.loadMoveList(saveCfg);
			game.loadSavedGame(savedGame);
		} /*
			 * else { for (int i = 0; i < moves.length; i++) moves[i] = -1; }
			 */
		networkGame = new NetworkGame(this);

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

}