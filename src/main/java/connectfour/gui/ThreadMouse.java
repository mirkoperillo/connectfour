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

public class ThreadMouse extends Thread {

	ApplicationWindow mf;

	public ThreadMouse(ApplicationWindow mf) {
		this.mf = mf;
	}

	public void run() {

		while (true) {

			synchronized (mf) {
				for (int i = 0; i < 7; i++)
					mf.graphicGrid.column[i].flag = true;
				try {
					mf.wait();
				} catch (Exception ex) {
				}
			}

			try {
				this.sleep(100);
			} catch (Exception exc) {
			}
		}

	}
}