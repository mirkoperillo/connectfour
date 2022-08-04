package connectfour.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import connectfour.gui.dialogs.NewGameDialog;
import connectfour.logic.CfgManager;
import connectfour.model.Game;
import connectfour.model.Level;

public class MenuGame extends JMenu {

	private static final long serialVersionUID = -8054253667305414054L;
	private ApplicationWindow window;
	private Game game;

	public MenuGame(ApplicationWindow window) {
		this.window = window;
		this.game = this.window.game;
		init();
	}

	private void init() {
		setBackground(SystemColor.control);
		setFont(new java.awt.Font("Dialog", 0, 11));
		setText("Game");

		JMenuItem menuGameNew = new JMenuItem();
		menuGameNew.setBackground(SystemColor.control);
		menuGameNew.setFont(new java.awt.Font("Dialog", 0, 11));
		menuGameNew.setText("New");
		menuGameNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(113, 0, false));
		menuGameNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuGameNew_actionPerformed(e);
			}
		});

		JMenuItem menuGameQuit = new JMenuItem();
		menuGameQuit.setBackground(SystemColor.control);
		menuGameQuit.setFont(new java.awt.Font("Dialog", 0, 11));
		menuGameQuit.setText("Quit");
		menuGameQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(81, java.awt.event.KeyEvent.ALT_MASK, false));
		menuGameQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					menuGameQuit_actionPerformed(e);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		add(menuGameNew);
		addSeparator();
		add(menuGameQuit);
	}

	/* attivazione di Game>New, apertura di una nuova partita con dialog */
	private void menuGameNew_actionPerformed(ActionEvent e) {
		/* apertura del dialog con le impostazioni di gioco */
		NewGameDialog dlg = new NewGameDialog(window, "", true);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.pack();
		dlg.setVisible(true);

		/* se viene premuto il tasto cancel nel dialog torniamo alla partita */
		if (!game.isBeginNewMatch())
			return;

		/* le componenti vengono reinizializzate */
		window.graphicGrid.cleanGrid();
		game.gameGrid.init();

		window.gameOver = false;
		window.removing = false;

		game.gameGrid.currentPlayer = 1;
		game.reset();

		window.graphicGrid.updateUI();
		if (!game.isNetworkGame()) {
			window.startNewLocalGame();
		}

		if (game.getLevel() == Level.HARD) {
			window.menuSettingsLevelNormal.setState(false);
			window.menuSettingsLevelHard.setState(true);
		} else {
			window.menuSettingsLevelNormal.setState(true);
			window.menuSettingsLevelHard.setState(false);
		}

		/* se abilitiamo la modalita' rete per la prima volta */
		if (window.game.isNetworkGame() && !window.game.isNetworkEnabled()) {
			/* apre il thread */
			Game g = new Game();
			// g.start();
			g.newGame();
		} else {
			/* se abbiamo appena finito di giocare in rete... */
			if (window.game.isNetworkEnabled()) {
				/* ...e vogliamo smettere */
				if (!window.game.isNetworkGame()) {
					window.game.disableNetwork();
//					try {
//						game.networkGame.s.close();
//					} catch (Exception ex) {
//					}
//					try {
//						game.networkGame.ss.close();
//					} catch (Exception exc) {
//					}
				}
				/* ...e vogliamo continuare con altri giocatori in rete */
				else {
//					try {
//						game.networkGame.s.close();
//					} catch (Exception ex) {
//					}
//					try {
//						game.networkGame.ss.close();
//					} catch (Exception exc) {
//					}
					game.disableNetwork();
					Game g = new Game(); // ???
					// g.start();
					g.newGame();
				}
			}
		}
		/* se il computer � il primo a dover giocare, lo fa */
		if (!game.getPlayer1().isHuman() && !game.isNetworkGame()) {
			window.play();
		}
	}

	/* attivazione di Game>Exit */
	private void menuGameQuit_actionPerformed(ActionEvent e) throws Exception {
		CfgManager cfgManager = new CfgManager();
		cfgManager.saveCfg(game);
		System.exit(0);
	}

}
