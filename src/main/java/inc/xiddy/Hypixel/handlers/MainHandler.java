package inc.xiddy.hypixel.handlers;

import inc.xiddy.hypixel.logging.Log;

public class MainHandler {
	private ScoreboardHandler boardHandler;
	private GameHandler gameHandler;

	public void initMainHandler() {
		// Set up Scoreboard handler
		Log.info("Registering scoreboard handler...");
		this.boardHandler = new ScoreboardHandler();

		// Set up Game handler
		Log.info("Registering game handler...");
		this.gameHandler = new GameHandler();
	}

	public ScoreboardHandler getBoardHandler() {
		return boardHandler;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
	}
}
