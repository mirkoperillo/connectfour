package connectfour.gui;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;

import connectfour.model.Game;

public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = 3100829130843054789L;
	private ApplicationWindow window;
	private Game game;

	private MenuGame menuGame;
	private MenuSettings menuSettings;
	private MenuMove menuMove;
	private MenuHelp menuHelp;

	public MenuBar(ApplicationWindow window) {
		this.window = window;
		this.game = window.game;
		init();
	}

	private void init() {
		setBackground(SystemColor.control);
		setFont(new java.awt.Font("Dialog", 0, 11));

		menuGame = new MenuGame(window);
		menuSettings = new MenuSettings(window);
		menuMove = new MenuMove(window);
		menuHelp = new MenuHelp(window);

		add(menuGame);
		add(menuMove);
		add(menuSettings);
		add(menuHelp);
	}

	public void menuMovePlay_actionPerformed(ActionEvent e) {
		window.play();
	}

	public void startNewLocalGame() {
		menuMove.startNewLocalGame();
	}

	public void disableMoveForward() {
		menuMove.disableMoveForward();
	}

	public void disablePlay() {
		menuMove.disablePlay();

	}

	public void disableMoveBack() {
		menuMove.disableMoveBack();
	}

	public void enableMoveForward() {
		menuMove.enableMoveForward();
	}

	public void enablePlay() {
		menuMove.enablePlay();

	}

	public void enableMoveBack() {
		menuMove.enableMoveBack();
	}

}
