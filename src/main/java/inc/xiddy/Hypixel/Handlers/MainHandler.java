package inc.xiddy.Hypixel.Handlers;

public class MainHandler {
	private DataHandler dataHandler;
	private ScoreboardHandler boardHandler;
	private MapHandler mapHandler;
	private LoggerHandler logger;
	private PlayerHandler playerHandler;
	private CommandHandler commandHandler;
	private ThreadHandler threadHandler;
	private GameHandler gameHandler;
	private AnticheatHandler anticheatHandler;

	public MainHandler() {
	}

	public void initMainHandler() {
		// Register logger
		System.out.println("Registering logger...");
		this.logger = new LoggerHandler();

		// Set up File handler
		this.getLogger().warning("Registering file handler...");
		this.dataHandler = new DataHandler("plugins/Hypixel");

		// Set up Scoreboard handler
		this.getLogger().warning("Registering scoreboard handler...");
		this.boardHandler = new ScoreboardHandler();

		// Set up Map handler
		this.getLogger().warning("Registering map handler...");
		this.mapHandler = new MapHandler();

		// Set up player handler
		this.getLogger().warning("Registering player handler...");
		this.playerHandler = new PlayerHandler();

		// Set up Command handler
		this.getLogger().warning("Registering command handler...");
		this.commandHandler = new CommandHandler();

		// Set up Anticheat
		this.getLogger().warning("Registering anticheat handler...");
		this.anticheatHandler = new AnticheatHandler(15);

		// Set up Game handler
		this.getLogger().warning("Registering game handler...");
		this.gameHandler = new GameHandler();

		// Set up thread handler
		this.getLogger().warning("Registering thread handler...");
		this.threadHandler = new ThreadHandler();
	}

	public DataHandler getDataHandler() {
		return this.dataHandler;
	}

	public ScoreboardHandler getBoardHandler() {
		return boardHandler;
	}

	public MapHandler getMapHandler() {
		return mapHandler;
	}

	public LoggerHandler getLogger() {
		return logger;
	}

	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public ThreadHandler getThreadHandler() {
		return threadHandler;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
	}

	public AnticheatHandler getAnticheatHandler() {
		return anticheatHandler;
	}
}
