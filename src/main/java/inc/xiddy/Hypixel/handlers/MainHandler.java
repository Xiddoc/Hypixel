package inc.xiddy.hypixel.handlers;

import inc.xiddy.hypixel.server.Tasks;
import inc.xiddy.hypixel.commons.Log;

public class MainHandler {
	private DataHandler dataHandler;
	private ScoreboardHandler boardHandler;
	private PlayerHandler playerHandler;
	private Tasks threadHandler;
	private GameHandler gameHandler;
	private AnticheatHandler anticheatHandler;

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

		// Set up Anticheat
		Log.warning("Registering anticheat handler...");
		this.anticheatHandler = new AnticheatHandler(15);

		// Set up Game handler
		Log.warning("Registering game handler...");
		this.gameHandler = new GameHandler();

		// Set up thread handler
		Log.warning("Registering thread handler...");
		this.threadHandler = new Tasks();
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

	public Tasks getThreadHandler() {
		return threadHandler;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
	}

	public AnticheatHandler getAnticheatHandler() {
		return anticheatHandler;
	}
}
