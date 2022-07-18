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
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.UIManager;

import connectfour.model.Grid;
import connectfour.model.Player;

public class Gui {

	/* variabili per la gestione del frame */
	boolean packFrame = false;
	public MainFrame frame;

	/* variabili per la gestione di gioco */
	Grid gameGrid;

	/* array con lista delle mosse */
	static int moves[];

	/* indice che definisce l'ultima mossa valida */
	static int marker;

	/* giocatore che inzia la partita */
	static Player one;

	/* secondo giocatore */
	static Player two;

	/* livello di gioco del computer */
	static int level;

	boolean beginNewMatch = true;

	/* variabili per la gestione di rete */

	/* abilita il gioco in rete */
	static boolean network;

	/* dichiara se la rete � stata abilitata */
	static boolean netenabled;

	/* identifica chi � il server */
	static boolean amIserver = false;

	/* gestisce il turno di chi parla e chi ascolta */
	static boolean neturn = false;

	static Socket s;
	static ServerSocket ss;

	/* nome o ip dell'avversario */
	static String hostName;

	/* colonna giocata e inviata */
	static int myColumn = -1;

	/* risposta ricevuta */
	static int column;

	/* canale di ricezione del server */
	static BufferedReader s_in;

	/* canale di trasmissione del server */
	static PrintWriter s_out;

	/* canale di ricezione del client */
	static BufferedReader c_in;

	/* canale di trasmissione del client */
	static PrintWriter c_out;

	public Gui() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		network = false;
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
		if (saveCfg.exists() && !network)
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
	public static void loadCfg(File cfg) {
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
						hostName = new String(tmp);
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
	public static void loadMoveList(File saved) {
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

	/* __________________________________________________________________________ */
	/* __________________________enable_network__________________________________ */
	/*
	 * creazione della partita in rete: vengono disabilitate le opzioni di gioco a a
	 * parte il fatto di potere reiniziare un'altra partita con altri giocatori rete
	 */
	public void enable_network() {
		netenabled = true;

		/*
		 * disabilitati i tasti ed i menu di modifica della configurazione della partita
		 */
		frame.menuMoveBack.setEnabled(false);
		frame.menuMovePlay.setEnabled(false);
		frame.menuMoveForward.setEnabled(false);
		frame.newGame.setEnabled(false);
		frame.moveBack.setEnabled(false);
		frame.playHint.setEnabled(false);
		frame.moveForward.setEnabled(false);
		// frame.menuSettingsLevel.setEnabled(false);

		/* nome o ip dell'avversario */
		InetAddress name;

		/* porta standard a cui connettersi per la partita */
		int serverPort = 1432;

		try {
			name = InetAddress.getByName(hostName);

			/*
			 * inizializzazione del socket: si cerca di connettersi al server con ip name,
			 * se esso non esiste si ricade nell'eccezione gestita in modo tale che il primo
			 * giocatore disponibile funga da server
			 */
			s = new Socket(name, serverPort);

			c_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			c_out = new PrintWriter(s.getOutputStream(), true);

			/* avviso di avventuta connessione */
			frame.statusBar.setText(" Connected to host " + s.getInetAddress().getHostName() + " at door: "
					+ s.getLocalPort() + " from: " + s.getPort());

			/*
			 * inizio della partita come client - la ricerca di un server non ha dato
			 * eccezione -
			 */
			while (true) {
				if (neturn) {
					/*
					 * il client pu� inviare la mossa; dopo di che termina il turno
					 */
					// FIXME network communication work good only with this
					// empty log message
					// probably deadlock somewhere in net workflow (tested only
					// on same machine)
					System.out.println("");
					if (myColumn != -1) {
						neturn = false;
						send(c_out, myColumn);
						myColumn = -1;
					}
				} else {
					/*
					 * il client � in ascolto: quando riceve la mossa aggiorna la propria griglia di
					 * gioco e riceve il turno
					 */
					column = receive(c_in);
					frame.graphicGrid.loadGrid(column);
					moves[marker] = column;
					marker++;
					neturn = true;
				}
			}
		} catch (UnknownHostException uhe) {
			frame.statusBar.setText(" Unknown host name...");
		}
		/* non trovando il server, il giocatore si offre di diventarlo */
		catch (ConnectException ce) {
			amIserver = true;
			neturn = false;
			frame.statusBar.setText(" Server not found, initializing new server...");
		}
		/* l'avversario ha terminato il programma, non c'� nessuno ad ascoltare */
		catch (SocketException se) {
			frame.statusBar.setText(" Net game is over (maybe no one hears you)");
		} catch (Exception e) {
			frame.statusBar.setText(" Net game is over (maybe no one hears you)");
		}

		/* inizializzazione del server */
		if (amIserver) {
			try {
				/* attesa del client */
				ss = new ServerSocket(serverPort);
				frame.statusBar.setText(" Waiting for your partner response...");
				s = ss.accept();
				frame.statusBar.setText(" Connected to host " + s.getInetAddress().getHostName() + " at door: "
						+ s.getLocalPort() + " from: " + s.getPort());

				/*
				 * appena il client si connette, la partita pu� iniziare ed il turno spetta a
				 * chi l'ha creata
				 */
				neturn = true;

				s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				s_out = new PrintWriter(s.getOutputStream(), true);

				while (true) {
					// FIXME network communication work good only with this
					// empty log message
					// probably deadlock somewhere in net workflow (tested only
					// on same machine)
					System.out.println("");
					if (neturn) {
						/*
						 * il server pu� inviare la mossa; dopo di che termina il turno
						 */
						if (myColumn != -1) {
							neturn = false;
							send(s_out, myColumn);
							myColumn = -1;
						}
					} else {
						/*
						 * il client � in ascolto: quando riceve la mossa aggiorna la propria griglia di
						 * gioco e riceve il turno
						 */
						column = receive(s_in);
						frame.graphicGrid.loadGrid(column);
						moves[marker] = column;
						marker++;
						neturn = true;
					}
				}
			}
			/*
			 * l'avversario ha terminato il programma, non c'� nessuno ad ascoltare
			 */
			catch (SocketException se) {
				frame.statusBar.setText(" Net game is over (maybe no one hears you)");
				se.printStackTrace();
			} catch (Exception e) {
				frame.statusBar.setText(" Net game is over (maybe no one hears you)");
				e.printStackTrace();
			}
		}
		// try{ s.close();}catch(Exception e){}
	}

	/* procedura di invio dati in rete */
	static void send(PrintWriter s_out, int c) {
		s_out.println(c);
	}

	/* procedura di ricezione dati in rete */
	static int receive(BufferedReader s_in) throws Exception {
		String buffer = s_in.readLine();
		return (buffer.charAt(0) - 48);
	}
}