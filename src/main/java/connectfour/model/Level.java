package connectfour.model;

/*

Copyright (C) 2022 Mirko Perillo

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

public enum Level {
	NORMAL(4), HARD(6);

	private int difficulty;

	private Level(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getDifficulty() {
		return difficulty;
	}

}
