package connectfour.gui;

import connectfour.logic.CfgManager;
import connectfour.model.Configuration;
import connectfour.model.Game;
import connectfour.model.SavedGame;

public class Gui {

	/* variabili per la gestione del frame */
	public MainFrame window;

	public Game game;

	private CfgManager cfgManager;

	public Gui() {
		cfgManager = new CfgManager();

		Configuration cfg = cfgManager.loadCfg();
		this.game = new Game(cfg);
		/*
		 * in rete non viene considerata l'ultima partita giocata; altrimenti viene
		 * caricata - se precedentemente non era stata conclusa
		 */
		if (!game.isNetworkGame()) {
			SavedGame savedGame = cfgManager.loadMoveList();
			game.loadSavedGame(savedGame);
		}
		// networkGame = new NetworkManager(this);

		window = new MainFrame(game);
	}

}