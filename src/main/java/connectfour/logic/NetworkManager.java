package connectfour.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import connectfour.gui.Gui;

public class NetworkManager {

	private Gui gui;

	public Socket s;
	public ServerSocket ss;

	/* colonna giocata e inviata */
	public int myColumn = -1;

	/* risposta ricevuta */
	public int column;

	/* canale di ricezione del server */
	private BufferedReader s_in;

	/* canale di trasmissione del server */
	private PrintWriter s_out;

	/* canale di ricezione del client */
	private BufferedReader c_in;

	/* canale di trasmissione del client */
	private PrintWriter c_out;

	/* gestisce il turno di chi parla e chi ascolta */
	public boolean neturn = false;

	/* dichiara se la rete è stata abilitata */
	// public boolean netenabled;

	/* identifica chi è il server */
	public boolean amIserver = false;

	/* nome o ip dell'avversario */
	public String hostName;

	public NetworkManager(Gui gui) {
		this.gui = gui;
	}

	/* __________________________________________________________________________ */
	/* __________________________enable_network__________________________________ */
	/*
	 * creazione della partita in rete: vengono disabilitate le opzioni di gioco a a
	 * parte il fatto di potere reiniziare un'altra partita con altri giocatori rete
	 */
	public void enableNetwork() {
		// netenabled = true;

		/*
		 * disabilitati i tasti ed i menu di modifica della configurazione della partita
		 */
		// FIXME move this step
		// FIXME gui.window.menuMoveBack.setEnabled(false);
		// FIXME gui.window.menuMovePlay.setEnabled(false);
		// FIXME gui.window.menuMoveForward.setEnabled(false);
		gui.window.newGame.setEnabled(false);
		gui.window.moveBack.setEnabled(false);
		gui.window.playHint.setEnabled(false);
		gui.window.moveForward.setEnabled(false);
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
			String connectionOkMsg = String.format("Connected to host %s, at door: %s from: %s",
					s.getInetAddress().getHostName(), s.getLocalPort(), s.getPort());
			gui.window.statusBarMsg(connectionOkMsg);

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
					gui.window.graphicGrid.loadGrid(column);
					gui.game.nextMove(column);
					neturn = true;
				}
			}
		} catch (UnknownHostException uhe) {
			gui.window.statusBarMsg("Unknown host name...");
		}
		/* non trovando il server, il giocatore si offre di diventarlo */
		catch (ConnectException ce) {
			amIserver = true;
			neturn = false;
			gui.window.statusBarMsg("Server not found, initializing new server...");
		}
		/* l'avversario ha terminato il programma, non c'� nessuno ad ascoltare */
		catch (SocketException se) {
			gui.window.statusBarMsg("Net game is over (maybe no one hears you)");
		} catch (Exception e) {
			gui.window.statusBarMsg("Net game is over (maybe no one hears you)");
		}

		/* inizializzazione del server */
		if (amIserver) {
			try {
				/* attesa del client */
				ss = new ServerSocket(serverPort);
				gui.window.statusBarMsg("Waiting for your partner response...");
				s = ss.accept();
				gui.window.statusBarMsg("Connected to host " + s.getInetAddress().getHostName() + " at door: "
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
						gui.window.graphicGrid.loadGrid(column);
						gui.game.nextMove(column);
						neturn = true;
					}
				}
			}
			/*
			 * l'avversario ha terminato il programma, non c'� nessuno ad ascoltare
			 */
			catch (SocketException se) {
				gui.window.statusBarMsg("Net game is over (maybe no one hears you)");
				se.printStackTrace();
			} catch (Exception e) {
				gui.window.statusBarMsg("Net game is over (maybe no one hears you)");
				e.printStackTrace();
			}
		}
		// try{ s.close();}catch(Exception e){}
	}

	public void disableNetwork() {
		try {
			s.close();
		} catch (Exception ex) {
		}
		try {
			ss.close();
		} catch (Exception exc) {
		}
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
