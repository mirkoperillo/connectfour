package connectfour.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import connectfour.gui.dialogs.PlayerNameDialog;
import connectfour.model.Game;
import connectfour.model.Level;

public class MenuSettings extends JMenu {

	private static final long serialVersionUID = 4818104178157011749L;
	private ApplicationWindow window;
	private Game game;

	public MenuSettings(ApplicationWindow window) {
		this.window = window;
		this.game = this.window.game;
		init();
	}

	private void init() {
		setBackground(SystemColor.control);
		setFont(new java.awt.Font("Dialog", 0, 11));
		setText("Settings");

		JMenu menuSettingsLevel = new JMenu();
		menuSettingsLevel.setBackground(SystemColor.control);
		menuSettingsLevel.setFont(new java.awt.Font("Dialog", 0, 11));
		menuSettingsLevel.setText("Level");

		/* costruzione del menu Level, voce Normal */
		JCheckBoxMenuItem menuSettingsLevelNormal = new JCheckBoxMenuItem();
		menuSettingsLevelNormal.setBackground(SystemColor.control);
		menuSettingsLevelNormal.setFont(new java.awt.Font("Dialog", 0, 11));
		menuSettingsLevelNormal.setText("Normal");
		menuSettingsLevelNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSettingsLevelNormal_actionPerformed(e);
			}
		});

		/* costruzione del sottomenu Level, voce Hard */
		JCheckBoxMenuItem menuSettingsLevelHard = new JCheckBoxMenuItem();
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

		menuSettingsLevel.add(menuSettingsLevelNormal);
		menuSettingsLevel.add(menuSettingsLevelHard);

		JMenuItem menuSettingsPlayername = new JMenuItem();
		menuSettingsPlayername.setBackground(SystemColor.control);
		menuSettingsPlayername.setFont(new java.awt.Font("Dialog", 0, 11));
		menuSettingsPlayername.setText("Player name");
		menuSettingsPlayername.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSettingsPlayername_actionPerformed(e);
			}
		});

		add(menuSettingsPlayername);
		add(menuSettingsLevel);
	}

	public void menuSettingsPlayername_actionPerformed(ActionEvent e) {
		PlayerNameDialog dlg = new PlayerNameDialog(window, "Players' names", true);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setVisible(true);
	}

	public void menuSettingsLevelHard_actionPerformed(ActionEvent e) {
		window.menuSettingsLevelNormal.setState(false);
		window.menuSettingsLevelHard.setState(true);
		game.setLevel(Level.HARD);
	}

	public void menuSettingsLevelNormal_actionPerformed(ActionEvent e) {
		window.menuSettingsLevelNormal.setState(true);
		window.menuSettingsLevelHard.setState(false);
		game.setLevel(Level.NORMAL);
	}
}
