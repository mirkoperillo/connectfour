package connectfour.gui;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import connectfour.model.Game;

public class MenuMove extends JMenu {

	private static final long serialVersionUID = 8151272731945500561L;
	private ApplicationWindow window;
	private Game game;

	private JMenuItem menuMoveBack;
	private JMenuItem menuMoveForward;
	private JMenuItem menuMovePlay;

	public MenuMove(ApplicationWindow window) {
		this.window = window;
		this.game = this.window.game;
		init();
	}

	private void init() {
		setBackground(SystemColor.control);
		setFont(new java.awt.Font("Dialog", 0, 11));
		setText("Move");

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

		add(menuMoveBack);
		add(menuMoveForward);
		add(menuMovePlay);
	}

	public void menuMoveBack_actionPerformed(ActionEvent e) {

	}

	/* attivazione di Move>Play */
	public void menuMovePlay_actionPerformed(ActionEvent e) {
		window.play();
	}

	/* attivazione di Move>Forward */
	public void menuMoveForward_actionPerformed(ActionEvent e) {
		window.moveForward();
	}

	public void startNewLocalGame() {
		menuMoveBack.setEnabled(false);
		menuMovePlay.setEnabled(true);
		menuMoveForward.setEnabled(false);
	}

	public void disableMoveForward() {
		menuMoveForward.setEnabled(false);
	}

	public void disablePlay() {
		menuMovePlay.setEnabled(false);
	}

	public void disableMoveBack() {
		menuMoveBack.setEnabled(false);
	}

	public void enableMoveForward() {
		menuMoveForward.setEnabled(true);
	}

	public void enablePlay() {
		menuMovePlay.setEnabled(true);
	}

	public void enableMoveBack() {
		menuMoveBack.setEnabled(true);
	}
}
