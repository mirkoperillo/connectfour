package connectfour.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import connectfour.gui.dialogs.AboutDialog;
import connectfour.model.Game;

public class MenuHelp extends JMenu {
	private static final long serialVersionUID = 1L;
	private ApplicationWindow window;
	private Game game;

	public MenuHelp(ApplicationWindow window) {
		this.window = window;
		this.game = this.window.game;
		init();
	}

	private void init() {
		setBackground(SystemColor.control);
		setFont(new java.awt.Font("Dialog", 0, 11));
		setText("?");

		/* costruzione del menu ?, voce About */
		JMenuItem menuHelpAbout = new JMenuItem();
		menuHelpAbout.setBackground(SystemColor.control);
		menuHelpAbout.setFont(new java.awt.Font("Dialog", 0, 11));
		menuHelpAbout.setText("About");
		menuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuHelpAbout_actionPerformed(e);
			}
		});

		/* costruzione del menu ?, voce Help */
		JMenuItem menuHelpHelp = new JMenuItem();
		menuHelpHelp.setBackground(SystemColor.control);
		menuHelpHelp.setFont(new java.awt.Font("Dialog", 0, 11));
		menuHelpHelp.setText("Help");
		menuHelpHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(112, 0, false));
		menuHelpHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuHelpHelp_actionPerformed(e);
			}
		});

		add(menuHelpAbout);
		add(menuHelpHelp);
	}

	public void menuHelpAbout_actionPerformed(ActionEvent e) {
		AboutDialog dlg = new AboutDialog(window);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setModal(true);
		dlg.setVisible(true);
	}

	public void menuHelpHelp_actionPerformed(ActionEvent e) {
		String htm = new File("").getAbsolutePath() + "/Help.htm";
		String command = new String("C:/Programmi/Internet Explorer/iexplore.exe " + htm);
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException ioe) {
			window.statusBarMsg("Couldn't open your browser...");
		}
	}
}
