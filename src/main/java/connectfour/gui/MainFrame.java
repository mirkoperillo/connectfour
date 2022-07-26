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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import connectfour.logic.CfgManager;
import connectfour.logic.EuristicTree;
import connectfour.model.Game;
import connectfour.model.Level;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 5468281161687521106L;

	ThreadMouse eventAvoider;
	/* flag che dichiara se la partita e' stata conclusa */
	public boolean gameOver = false;

	/*
	 * flag che dichiara se stiamo modificando una configurazione: la modifica si
	 * interrompe non appena inseriamo un nuovo gettone
	 */
	public boolean removing = false;

	/* l'applicazione che ha aperto il frame */
	// Gui launcherApp;
	Game game;

	/* dichiarazione dei pannelli che costituiscono la finestra principale */
	JPanel contentPane;
	public GraphicGrid graphicGrid;
	BorderLayout borderLayout;

	/* dichiarazione della barra di menu e dei relativi sottomenu */
	JMenuBar menuBar;
	JMenu menuGame;
	JMenuItem menuGameNew;
	JMenuItem menuGameQuit;
	JMenu menuMove;
	public JMenuItem menuMoveBack;
	public JMenuItem menuMovePlay;
	public JMenuItem menuMoveForward;
	JMenu menuSettings;
	JMenu menuSettingsLevel;
	JCheckBoxMenuItem menuSettingsLevelNormal;
	JCheckBoxMenuItem menuSettingsLevelHard;
	JMenuItem menuSettingsPlayername;
	JMenu menuHelp;
	JMenuItem menuHelpAbout;
	JMenuItem menuHelpHelp;

	/* dichiarazione della toolbar e delle relative icone */
	JToolBar toolBar;
	public JButton newGame;
	public JButton moveBack;
	public JButton playHint;
	public JButton moveForward;

	/*
	 * dichiarazione della barra di stato: essa fornisce utili informazioni durante
	 * il gioco
	 */
	public JLabel statusBar = new JLabel(" ");

	public MainFrame(Game game) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		eventAvoider = new ThreadMouse(this);

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		// launcherApp = launcher;
		this.game = game;
		/* invocazione della funzione di inizializzazione */
		try {
			jbInit();
			eventAvoider.start();
			this.validate();
			/* centra il frame rispetto allo schermo */
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = getSize();
			if (frameSize.height > screenSize.height)
				frameSize.height = screenSize.height;
			if (frameSize.width > screenSize.width)
				frameSize.width = screenSize.width;
			setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

			/* il frame non � ridimensionabile */
			setResizable(false);

			/* il frame � sempre visualizzato */
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * procedura di inizializzazione: crea in successione i menu con le relative
	 * voci, la toolbar, la griglia virtuale di gioco
	 */
	private void jbInit() throws Exception {
		/* costruzione degli oggetti e in seguito del frame */

		/* barra di stato (in basso) utile per messaggi all'utente */
		statusBar.setFont(new java.awt.Font("Dialog", 0, 11));
		statusBar.setBackground(SystemColor.control);

		/* inizializzazione della barra di menu */
		menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.control);
		menuBar.setFont(new java.awt.Font("Dialog", 0, 11));

		/* costruzione del menu Game */
		menuGame = new JMenu();
		menuGame.setBackground(SystemColor.control);
		menuGame.setFont(new java.awt.Font("Dialog", 0, 11));
		menuGame.setText("Game");

		/* costruzione del menu Game, voce New */
		menuGameNew = new JMenuItem();
		menuGameNew.setBackground(SystemColor.control);
		menuGameNew.setFont(new java.awt.Font("Dialog", 0, 11));
		menuGameNew.setText("New");
		menuGameNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(113, 0, false));
		menuGameNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuGameNew_actionPerformed(e);
			}
		});

		/* costruzione del menu Game, voce Quit */
		menuGameQuit = new JMenuItem();
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

		/* costruzione del menu Move */
		menuMove = new JMenu();
		menuMove.setBackground(SystemColor.control);
		menuMove.setFont(new java.awt.Font("Dialog", 0, 11));
		menuMove.setText("Move");

		/* costruzione del menu Move, voce Back */
		menuMoveBack = new JMenuItem();
		menuMoveBack.setBackground(SystemColor.control);
		menuMoveBack.setFont(new java.awt.Font("Dialog", 0, 11));
		menuMoveBack.setText("Back");
		menuMoveBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuMoveBack_actionPerformed(e);
			}
		});
		menuMoveBack.setEnabled(false);

		/* costruzione del menu Move, voce Play */
		menuMovePlay = new JMenuItem();
		menuMovePlay.setBackground(SystemColor.control);
		menuMovePlay.setFont(new java.awt.Font("Dialog", 0, 11));
		menuMovePlay.setText("Play");
		menuMovePlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuMovePlay_actionPerformed(e);
			}
		});

		/* costruzione del menu Move, voce Forward */
		menuMoveForward = new JMenuItem();
		menuMoveForward.setBackground(SystemColor.control);
		menuMoveForward.setFont(new java.awt.Font("Dialog", 0, 11));
		menuMoveForward.setText("Forward");
		menuMoveForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuMoveForward_actionPerformed(e);
			}
		});
		menuMoveForward.setEnabled(false);

		/* costruzione del menu, voce Settings */
		menuSettings = new JMenu();
		menuSettings.setBackground(SystemColor.control);
		menuSettings.setFont(new java.awt.Font("Dialog", 0, 11));
		menuSettings.setText("Settings");

		/* costruzione del menu Settings, voce Level */
		menuSettingsLevel = new JMenu();
		menuSettingsLevel.setBackground(SystemColor.control);
		menuSettingsLevel.setFont(new java.awt.Font("Dialog", 0, 11));
		menuSettingsLevel.setText("Level");

		/* costruzione del menu Level, voce Normal */
		menuSettingsLevelNormal = new JCheckBoxMenuItem();
		menuSettingsLevelNormal.setBackground(SystemColor.control);
		menuSettingsLevelNormal.setFont(new java.awt.Font("Dialog", 0, 11));
		menuSettingsLevelNormal.setText("Normal");
		menuSettingsLevelNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSettingsLevelNormal_actionPerformed(e);
			}
		});

		/* costruzione del sottomenu Level, voce Hard */
		menuSettingsLevelHard = new JCheckBoxMenuItem();
		menuSettingsLevelHard.setBackground(SystemColor.control);
		menuSettingsLevelHard.setFont(new java.awt.Font("Dialog", 0, 11));
		menuSettingsLevelHard.setText("Hard");
		menuSettingsLevelHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSettingsLevelHard_actionPerformed(e);
			}
		});
		if (game.getLevel() == Level.HARD) {
			menuSettingsLevelNormal.setState(false);
			menuSettingsLevelHard.setState(true);
		} else {
			menuSettingsLevelNormal.setState(true);
			menuSettingsLevelHard.setState(false);
		}

		/* costruzione del menu Settings, voce Playername */
		menuSettingsPlayername = new JMenuItem();
		menuSettingsPlayername.setBackground(SystemColor.control);
		menuSettingsPlayername.setFont(new java.awt.Font("Dialog", 0, 11));
		menuSettingsPlayername.setText("Player name");
		menuSettingsPlayername.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSettingsPlayername_actionPerformed(e);
			}
		});

		/* costruzione del menu ? */
		menuHelp = new JMenu();
		menuHelp.setBackground(SystemColor.control);
		menuHelp.setFont(new java.awt.Font("Dialog", 0, 11));
		menuHelp.setText("?");

		/* costruzione del menu ?, voce About */
		menuHelpAbout = new JMenuItem();
		menuHelpAbout.setBackground(SystemColor.control);
		menuHelpAbout.setFont(new java.awt.Font("Dialog", 0, 11));
		menuHelpAbout.setText("About");
		menuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuHelpAbout_actionPerformed(e);
			}
		});

		/* costruzione del menu ?, voce Help */
		menuHelpHelp = new JMenuItem();
		menuHelpHelp.setBackground(SystemColor.control);
		menuHelpHelp.setFont(new java.awt.Font("Dialog", 0, 11));
		menuHelpHelp.setText("Help");
		menuHelpHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(112, 0, false));
		menuHelpHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuHelpHelp_actionPerformed(e);
			}
		});

		/* inizializzazione della toolbar */
		toolBar = new JToolBar();
		toolBar.setBackground(SystemColor.control);
		toolBar.setFloatable(false);
		toolBar.setBorderPainted(false);
		toolBar.setOrientation(SwingConstants.HORIZONTAL);

		/* inizializzazione delle icone della toolbar */
		newGame = new JButton(new ImageIcon(getClass().getResource("/icons/new_disabled.gif")));
		newGame.setRolloverEnabled(true);
		newGame.setRolloverIcon(new ImageIcon(getClass().getResource("/icons/new_enabled.gif")));
		newGame.setRequestFocusEnabled(false);
		newGame.setBorderPainted(false);
		newGame.setToolTipText("New game");
		newGame.setBorder(BorderFactory.createRaisedBevelBorder());
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					menuQuickNew_actionPerformed(e);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		moveBack = new JButton(new ImageIcon(getClass().getResource("/icons/back_disabled.gif")));
		moveBack.setRolloverEnabled(true);
		moveBack.setRolloverIcon(new ImageIcon(getClass().getResource("/icons/back_enabled.gif")));
		moveBack.setRequestFocusEnabled(false);
		moveBack.setBorderPainted(false);
		moveBack.setToolTipText("Undo");
		moveBack.setBorder(BorderFactory.createRaisedBevelBorder());
		moveBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					menuMoveBack_actionPerformed(e);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		moveBack.setEnabled(false);

		playHint = new JButton(new ImageIcon(getClass().getResource("/icons/play_disabled.gif")));
		playHint.setRolloverEnabled(true);
		playHint.setRolloverIcon(new ImageIcon(getClass().getResource("/icons/play_enabled.gif")));
		playHint.setRequestFocusEnabled(false);
		playHint.setBorderPainted(false);
		playHint.setToolTipText("Hint");
		playHint.setBorder(BorderFactory.createRaisedBevelBorder());
		playHint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					menuMovePlay_actionPerformed(e);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		moveForward = new JButton(new ImageIcon(getClass().getResource("/icons/forward_disabled.gif")));
		moveForward.setRolloverEnabled(true);
		moveForward.setRolloverIcon(new ImageIcon(getClass().getResource("/icons/forward_enabled.gif")));
		moveForward.setRequestFocusEnabled(false);
		moveForward.setBorderPainted(false);
		moveForward.setToolTipText("Redo");
		moveForward.setBorder(BorderFactory.createRaisedBevelBorder());
		moveForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					menuMoveForward_actionPerformed(e);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		moveForward.setEnabled(false);

		contentPane = (JPanel) this.getContentPane();

		/* assemblaggio dei menu */
		menuGame.add(menuGameNew);
		menuGame.addSeparator();
		menuGame.add(menuGameQuit);
		menuBar.add(menuGame);
		menuMove.add(menuMoveBack);
		menuMove.add(menuMovePlay);
		menuMove.add(menuMoveForward);
		menuBar.add(menuMove);
		menuSettings.add(menuSettingsPlayername);
		menuSettingsLevel.add(menuSettingsLevelNormal);
		menuSettingsLevel.add(menuSettingsLevelHard);
		menuSettings.add(menuSettingsLevel);
		menuBar.add(menuSettings);
		menuBar.add(menuHelp);
		menuHelp.add(menuHelpAbout);
		menuHelp.add(menuHelpHelp);
		this.setJMenuBar(menuBar);

		/* assemblaggio della toolbar */
		toolBar.add(newGame);
		toolBar.addSeparator();
		toolBar.add(moveBack);
		toolBar.add(playHint);
		toolBar.add(moveForward);
		toolBar.addSeparator();

		/* costruzione della griglia grafica */
		graphicGrid = new GraphicGrid(this);
		graphicGrid.setPreferredSize(new Dimension(340, 355));

		/* assemblaggio del frame */
		setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/C4.gif")));
		setTitle("Connect 4 XP");
		setSize(350, 380);

		/* assemblaggio del pannello contentPane */
		contentPane.setSize(new Dimension(this.getWidth(), this.getHeight()));

		borderLayout = new BorderLayout(0, 0);
		contentPane.setLayout(borderLayout);
		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(graphicGrid, BorderLayout.CENTER);
		contentPane.add(statusBar, BorderLayout.SOUTH);

		/* conclusione dell'inizializzazione (con successo) */
		String msg = new String();
		msg = " Initilization completed";
		statusBar.setText(msg);

		/* caricamento di eventuale partita non terminata */
		if (game.getMarker() > 0 && !game.isNetworkEnabled()) {
			loadGame();
			/* se tocca al giocatore 1 che � computer, gioca */
			if (game.gameGrid.currentPlayer > 0 && !game.getPlayer1().isHuman())
				this.menuMovePlay_actionPerformed(null);
			else if (game.gameGrid.currentPlayer < 0 && !game.getPlayer2().isHuman())
				this.menuMovePlay_actionPerformed(null);
		} else {
			/* se il primo giocatore � il computer, gioca la sua mossa */
			if (!game.getPlayer1().isHuman() && !game.isNetworkEnabled())
				this.menuMovePlay_actionPerformed(null);
		}
	}

	/*
	 * carica l'ultima partita giocata non portata a termine dal vettore moves[],
	 * inserendo i gettoni nella griglia tramite il metodo loadGrid
	 */
	void loadGame() {
		for (int i = 0; i < game.getMarker(); i++) {
			graphicGrid.loadGrid(game.getMoves()[i]);
		}
	}

	/* avvio veloce dalla toolbar */
	public void menuQuickNew_actionPerformed(ActionEvent e) {
		/*
		 * in rete non � concesso iniziare una partita quando si vuole; � automatico
		 * dopo la fine di una partita
		 */
		if (game.isNetworkEnabled() && !gameOver)
			return;

		/* svuota la griglia grafica virtuale */
		graphicGrid.cleanGrid();

		/* svuota la griglia fisica */
		game.gameGrid.init();

		/* reinizializza le variabili e la lista delle mosse attive */
		gameOver = false;
		removing = false;
		game.gameGrid.currentPlayer = 1;
		if (game.isNetworkEnabled())
			game.gameGrid.currentPlayer *= -1;

		/* svuotato il vettore mosse */
		game.reset();
		graphicGrid.updateUI();

		/* servizi disabilitati in modalita' rete */
		if (!game.isNetworkEnabled()) {
			menuMoveBack.setEnabled(false);
			menuMovePlay.setEnabled(true);
			menuMoveForward.setEnabled(false);
			playHint.setEnabled(true);
			moveForward.setEnabled(false);
		}

		/* livello corrente di gioco */
		if (game.getLevel() == Level.HARD) {
			menuSettingsLevelNormal.setState(false);
			menuSettingsLevelHard.setState(true);
		} else {
			menuSettingsLevelNormal.setState(true);
			menuSettingsLevelHard.setState(false);
		}

		/* se tocca al computer, esso gioca la propria mossa */
		if (!game.getPlayer1().isHuman()) {
			this.menuMovePlay_actionPerformed(null);
		}
	}

	/* attivazione di Game>New, apertura di una nuova partita con dialog */
	public void menuGameNew_actionPerformed(ActionEvent e) {
		/* apertura del dialog con le impostazioni di gioco */
		MainFrame_NewGameBox dlg = new MainFrame_NewGameBox((JFrame) this, "", true);
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
		graphicGrid.cleanGrid();
		game.gameGrid.init();

		gameOver = false;
		removing = false;

		game.gameGrid.currentPlayer = 1;
		game.reset();

		graphicGrid.updateUI();
		if (!game.isNetworkGame()) {
			newGame.setEnabled(true);
			menuMoveBack.setEnabled(false);
			menuMovePlay.setEnabled(true);
			menuMoveForward.setEnabled(false);
			playHint.setEnabled(true);
			moveForward.setEnabled(false);
		}

		if (game.getLevel() == Level.HARD) {
			menuSettingsLevelNormal.setState(false);
			menuSettingsLevelHard.setState(true);
		} else {
			menuSettingsLevelNormal.setState(true);
			menuSettingsLevelHard.setState(false);
		}

		/* se abilitiamo la modalita' rete per la prima volta */
		if (game.isNetworkGame() && !game.isNetworkEnabled()) {
			/* apre il thread */
			Game g = new Game();
			// g.start();
			g.newGame();
		} else {
			/* se abbiamo appena finito di giocare in rete... */
			if (game.isNetworkEnabled()) {
				/* ...e vogliamo smettere */
				if (!game.isNetworkGame()) {
					game.disableNetwork();
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
			this.menuMovePlay_actionPerformed(null);
		}
	}

	/* attivazione di Game>Exit */
	public void menuGameQuit_actionPerformed(ActionEvent e) throws Exception {

		CfgManager cfgManager = new CfgManager();
		cfgManager.saveCfg(game);

		System.exit(0);
	}

	/* attivazione di Move>Back */
	public void menuMoveBack_actionPerformed(ActionEvent e) {
		/* se la griglia non e' vuota possiamo togliere l'ultimo gettone */
		if (game.getMarker() > 0) {
			removing = true;
			/* se prima la partita era finita puo' essere rigiocata */
			gameOver = false;
			/*
			 * se la partita e' contro un pc, torna indietro fino alla nostra mossa
			 * precedente, ossia di due mosse; altrimenti di una
			 */
			if (game.gameGrid.currentPlayer > 0) {
				if (!game.getPlayer2().isHuman() && game.getPlayer1().isHuman()) {
					if (game.getMarker() >= 2) {
						graphicGrid.removeLast();
						graphicGrid.removeLast();
					}
				} else
					graphicGrid.removeLast();
			}
			/* la stessa cosa per il giocatore 2 */
			else {
				if (game.getPlayer2().isHuman() && !game.getPlayer1().isHuman()) {
					if (game.getMarker() >= 2) {
						graphicGrid.removeLast();
						graphicGrid.removeLast();
					}
				} else
					graphicGrid.removeLast();
			}
		}
		/* dalla griglia vuota non e' possibile tornare indietro */
		if (game.getMarker() <= 0) {
			menuMoveBack.setEnabled(false);
			moveBack.setEnabled(false);
		} else {
			menuMoveBack.setEnabled(true);
			moveBack.setEnabled(true);
		}
		menuMoveForward.setEnabled(true);
		menuMovePlay.setEnabled(true);
		moveForward.setEnabled(true);
		playHint.setEnabled(true);
	}

	/* attivazione di Move>Play */
	public void menuMovePlay_actionPerformed(ActionEvent e) {
		/*
		 * se la partita e' ancora aperta, si crea l'albero delle scelte, quindi si fa
		 * scegliere la mossa all'intelligenza artificiale
		 */
		if (!gameOver) {
			EuristicTree tmpTree = new EuristicTree();
			tmpTree.build(game.gameGrid, game.gameGrid.currentPlayer, game.getLevel());
			int toPlay = tmpTree.play(game.gameGrid.currentPlayer);
			graphicGrid.loadGrid(toPlay);
			game.nextMove(toPlay);
			if (game.gameGrid.currentPlayer > 0) {
				if (!game.getPlayer1().isHuman() && game.getPlayer2().isHuman()) {
					menuMovePlay_actionPerformed(null);
				}
			} else {
				if (!game.getPlayer2().isHuman() && game.getPlayer1().isHuman()) {
					menuMovePlay_actionPerformed(null);
				}
			}
		}

		/* se la partita non e' terminata la funzione e' abilitata */
		if (game.getMarker() < 42) {
			if (game.getMoves()[game.getMarker()] == -1)
				if (e == null) {
					menuMoveForward.setEnabled(false);
					moveForward.setEnabled(false);
				}
		} else {
			menuMoveForward.setEnabled(false);
			menuMovePlay.setEnabled(false);
			moveForward.setEnabled(false);
			playHint.setEnabled(false);
		}
	}

	/* attivazione di Move>Forward */
	public void menuMoveForward_actionPerformed(ActionEvent e) {
		/*
		 * la funzionalita' avanti viene abilitata quando si torna indietro; si
		 * disabilita in rete sempre, a fine partita, o quando viene fatta
		 * un'inserzione. Se il computer ha gia' giocato, si reinseriscono due gettoni.
		 */
		if (game.getMoves()[game.getMarker()] != -1) {
			graphicGrid.loadGrid(game.lastMove());
		}

		if (game.gameGrid.currentPlayer > 0) {
			if (!game.getPlayer1().isHuman() && game.getPlayer2().isHuman()) {
				menuMovePlay_actionPerformed(null);
			}
		} else {
			if (!game.getPlayer2().isHuman() && game.getPlayer1().isHuman()) {
				menuMovePlay_actionPerformed(null);
			}
		}

		if (game.getMarker() < 41) {
			if (game.getMoves()[game.getMarker()] == -1) {
				menuMoveForward.setEnabled(false);
				moveForward.setEnabled(false);
			}
		} else {
			menuMoveForward.setEnabled(false);
			menuMovePlay.setEnabled(false);
			moveForward.setEnabled(false);
			playHint.setEnabled(false);
		}
	}

	/* attivazione Settings>Level>Normal */
	public void menuSettingsLevelNormal_actionPerformed(ActionEvent e) {
		menuSettingsLevelNormal.setState(true);
		menuSettingsLevelHard.setState(false);
		game.setLevel(Level.NORMAL);
	}

	/* attivazione Settings>Level>Normal */
	public void menuSettingsLevelHard_actionPerformed(ActionEvent e) {
		menuSettingsLevelNormal.setState(false);
		menuSettingsLevelHard.setState(true);
		game.setLevel(Level.HARD);
	}

	/* attivazione Settings>Playername */
	public void menuSettingsPlayername_actionPerformed(ActionEvent e) {
		MainFrame_PlayerNameBox dlg = new MainFrame_PlayerNameBox(this, "Players' names", true);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setVisible(true);
	}

	/* attivazione ?>About */
	public void menuHelpAbout_actionPerformed(ActionEvent e) {
		MainFrame_AboutBox dlg = new MainFrame_AboutBox(this);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setModal(true);
		dlg.setVisible(true);
	}

	/* attivazione di ?>Help */
	public void menuHelpHelp_actionPerformed(ActionEvent e) {
		String htm = new File("").getAbsolutePath() + "/Help.htm";
		String command = new String("C:/Programmi/Internet Explorer/iexplore.exe " + htm);
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException ioe) {
			statusBar.setText(" Couldn't open your browser...");
		}
	}

	/* metodo di chiusura finestra sovrascritto in Game>Quit */
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try {
				menuGameQuit_actionPerformed(null);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/* creazione messaggio di vittoria o sconfitta */
	void gameOver(String msg) {
		MainFrame_GameOverBox dlg = new MainFrame_GameOverBox(this, msg);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setModal(true);
		dlg.setVisible(true);
	}
}