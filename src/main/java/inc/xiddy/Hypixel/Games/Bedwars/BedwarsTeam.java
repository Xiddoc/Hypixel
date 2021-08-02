package inc.xiddy.Hypixel.Games.Bedwars;

import inc.xiddy.Hypixel.Constants.TeamColor;
import inc.xiddy.Hypixel.Dataclasses.GameMap;
import inc.xiddy.Hypixel.Dataclasses.SmallLocation;
import inc.xiddy.Hypixel.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.FileNotFoundException;
import java.util.*;

public class BedwarsTeam {
	private final TeamColor color;
	private final Map<Player, BedwarsState> players;
	private final Location generatorLocation;
	private final Location itemshopLocation;
	private final Location teamshopLocation;
	private final Location respawnLocation;
	private final Location bedLocation;
	private final GameMap map;
	private final int teamSize;
	private boolean hasBed = true;

	public BedwarsTeam(TeamColor color, GameMap map, int teamSize) throws FileNotFoundException {
		// Set data to fields
		this.color = color;
		this.map = map;
		this.teamSize = teamSize;
		this.players = new HashMap<>();
		// Pull stored data regarding locations
		this.generatorLocation = this.getBedwarsMarkerLocation("gen");
		this.itemshopLocation = this.getBedwarsMarkerLocation("itemshop");
		this.teamshopLocation = this.getBedwarsMarkerLocation("teamshop");
		this.respawnLocation = this.getBedwarsMarkerLocation("respawn");
		this.bedLocation = this.getBedwarsMarkerLocation("bed");
	}

	private Location getBedwarsMarkerLocation(String markerName) throws FileNotFoundException {
		// Get the location for the marker
		Location loc = Main.getMainHandler().getDataHandler().read("bedwars\\maps\\" + this.getMap().getMapName() + "\\" + this.getTeamColor() + "\\" + markerName + ".json", SmallLocation.class).toLocation();
		// Update the world
		loc.setWorld(this.getMap().getWorld());
		// Return the world
		return loc;
	}

	public boolean isTeamFull() {
		return this.getPlayerCount() == this.getTeamSize();
	}

	public int getPlayerCount() {
		return this.getPlayers().toArray(new Player[0]).length;
	}

	public Set<Player> getPlayers() {
		return this.players.keySet();
	}

	public Map<Player, BedwarsState> getPlayerAliveMap() {
		return this.players;
	}

	public void setPlayerState(Player player, BedwarsState state) {
		this.getPlayerAliveMap().put(player, state);
	}

	public void addPlayer(Player player) {
		this.players.put(player, BedwarsState.ALIVE);
	}

	public boolean isEliminated() {
		// If there are no alive (respawning or alive) players, then they are eliminated
		return this.getAlivePlayers().isEmpty();
	}

	public Set<Player> getAlivePlayers() {
		// For each player in the map
		// Filter by the .getValue of the map (the value stating if the player is alive)
		Set<Player> players = new HashSet<>();
		for (Map.Entry<Player, BedwarsState> entry : this.getPlayerAliveMap().entrySet()) {
			// If not dead (spectating)
			if (!entry.getValue().equals(BedwarsState.SPECTATING)) {
				players.add(entry.getKey());
			}
		}
		// Return the player set
		return players;
	}

	public TeamColor getTeamColor() {
		return this.color;
	}

	public ItemStack getWool(int amount) {
		//noinspection deprecation
		return new ItemStack(Material.WOOL, amount, this.getTeamColor().getDyeColor().getWoolData());
	}

	public boolean hasBed() {
		return this.hasBed;
	}

	public void setHasBed(boolean hasBed) {
		this.hasBed = hasBed;
	}

	public Location getGeneratorLocation() {
		return generatorLocation.clone();
	}

	public Location getItemshopLocation() {
		return itemshopLocation.clone();
	}

	public Location getTeamshopLocation() {
		return teamshopLocation.clone();
	}

	public Location getRespawnLocation() {
		return respawnLocation.clone();
	}

	public Location getBedLocation() {
		return bedLocation.clone();
	}

	public Location[] getBedLocations() {
		return new Location[]{this.getBedLocation(), this.getBedLocation().add(this.getBedLocation().getDirection().normalize())};
	}

	public GameMap getMap() {
		return map;
	}

	public int getTeamSize() {
		return teamSize;
	}

	public void removeBed() {
		// Remove it off the board
		this.setHasBed(false);
		// Remove blocks
		this.getBedLocations()[0].getBlock().setType(Material.AIR);
		this.getBedLocations()[1].getBlock().setType(Material.AIR);
	}

	public void generateBed() {
		// Add to the board
		this.setHasBed(true);
		// Get block states
		BlockState bedFoot = this.getBedLocations()[0].getBlock().getState();
		BlockState bedHead = this.getBedLocations()[1].getBlock().getState();
		// Set material type
		bedFoot.setType(Material.BED_BLOCK);
		bedHead.setType(Material.BED_BLOCK);
		// Convert direction to bytes
		int direction;
		switch (bedFoot.getBlock().getFace(bedHead.getBlock())) {
			default: {
				direction = 0x0;
				break;
			}
			case WEST: {
				direction = 0x1;
				break;
			}
			case NORTH: {
				direction = 0x2;
				break;
			}
			case EAST: {
				direction = 0x3;
				break;
			}
		}
		// Set byte data to blocks
		// 0x0 = Bed Foot (No XOR needed since XOR of 0x0 is the same value)
		// 0x8 = Bed Head
		//noinspection deprecation
		bedFoot.setRawData((byte) direction);
		//noinspection deprecation
		bedHead.setRawData((byte) (0x8 | (byte) direction));
		// Update the blocks
		bedFoot.update(true, false);
		bedHead.update(true, false);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BedwarsTeam that = (BedwarsTeam) o;
		return color.equals(that.color) && Objects.equals(players, that.players);
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, players);
	}
}
