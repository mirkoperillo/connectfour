package connectfour.model;

public record Configuration(boolean singlePlayerGame, boolean usePlayer1, String player1Name, String player2Name,
		String hostname, Level level) {
}
