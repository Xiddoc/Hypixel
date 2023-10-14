package inc.xiddy.hypixel.handlers;

import inc.xiddy.hypixel.logging.Log;

public class MainHandler {
	private DataHandler dataHandler;
	private ScoreboardHandler boardHandler;
	private PlayerHandler playerHandler;
	private GameHandler gameHandler;

	public void initMainHandler() {
		// Set up File handler
		Log.warning("Registering file handler...");
		this.dataHandler = new DataHandler("plugins/Hypixel");

		// Set up Scoreboard handler
		Log.warning("Registering scoreboard handler...");
		this.boardHandler = new ScoreboardHandler();

		// Set up player handler
		Log.warning("Registering player handler...");
		this.playerHandler = new PlayerHandler();

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

	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
	}
}
