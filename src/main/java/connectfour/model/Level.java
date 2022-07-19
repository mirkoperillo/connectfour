package connectfour.model;

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
