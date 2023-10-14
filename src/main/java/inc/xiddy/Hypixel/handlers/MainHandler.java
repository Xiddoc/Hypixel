package inc.xiddy.hypixel.handlers;

import inc.xiddy.hypixel.logging.Log;

public class MainHandler {
	private DataHandler dataHandler;
	private ScoreboardHandler boardHandler;
	private GameHandler gameHandler;

	public void initMainHandler() {
		// Set up File handler
		Log.warning("Registering file handler...");
		this.dataHandler = new DataHandler("plugins/Hypixel");

		// Set up Scoreboard handler
		Log.warning("Registering scoreboard handler...");
		this.boardHandler = new ScoreboardHandler();

		// Set up Game handler
		Log.warning("Registering game handler...");
		this.gameHandler = new GameHandler();
	}

	public DataHandler getDataHandler() {
		return this.dataHandler;
	}

	public ScoreboardHandler getBoardHandler() {
		return boardHandler;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
	}
}
