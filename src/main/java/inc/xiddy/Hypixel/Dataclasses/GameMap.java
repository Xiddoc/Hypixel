package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Utility.HypixelUtils;
import org.bukkit.World;

public class GameMap {
	private final Lobby game;
	private final String mapName;
	private World world;

	public GameMap(Lobby game, String mapName, World world) {
		this.game = game;
		this.mapName = mapName;
		this.world = world;
	}

	public GameMap(Lobby game, String mapName) {
		this(game, mapName, null);
	}

	public static String getPathToGameMaps(Lobby lobby) {
		return lobby.toString().toLowerCase() + "\\maps";
	}

	public String getPathToMapWorld() {
		return this.getPathToMap() + "\\world";
	}

	public String getPathToMapGlobals() {
		return this.getPathToMap() + "\\global";
	}

	public String getPathToMap() {
		return this.getGameAsString() + "\\maps\\" + this.getMapName();
	}

	public String getGameAsString() {
		return this.getGame().toString().toLowerCase();
	}

	public Lobby getGame() {
		return this.game;
	}

	public String getMapName() {
		return this.mapName;
	}

	public String getCapitalizedMapName() {
		return HypixelUtils.capitalize(this.getMapName());
	}

	public World getWorld() {
		return this.world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
