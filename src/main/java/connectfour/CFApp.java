package connectfour;

import connectfour.gui.Gui;
import connectfour.model.Game;

public class CFApp {

	public static void main(String[] args) {
		new Gui(new Game());
	}

}
