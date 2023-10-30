package inc.xiddy.hypixel.games.bedwars;

import inc.xiddy.hypixel.constants.TeamColor;
import inc.xiddy.hypixel.dataclasses.SmallLocation;
import inc.xiddy.hypixel.games.basegame.HypixelTeam;
import inc.xiddy.hypixel.games.basegame.maps.GameMap;
import inc.xiddy.hypixel.games.bedwars.state.BedwarsState;
import inc.xiddy.hypixel.handlers.DataHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

import java.io.FileNotFoundException;

public class BedwarsTeam extends HypixelTeam {
	private final Location generatorLocation;
	private final Location itemshopLocation;
	private final Location teamshopLocation;
	private final Location respawnLocation;
	private final Location bedLocation;
	private boolean hasBed = true;

	public BedwarsTeam(TeamColor color, GameMap map, int teamSize) throws FileNotFoundException {
		// Set data to fields
		super(color, map, teamSize);
		this.isTeamFull();
		// Pull stored data regarding locations
		this.generatorLocation = this.getBedwarsMarkerLocation("gen");
		this.itemshopLocation = this.getBedwarsMarkerLocation("itemshop");
		this.teamshopLocation = this.getBedwarsMarkerLocation("teamshop");
		this.respawnLocation = this.getBedwarsMarkerLocation("respawn");
		this.bedLocation = this.getBedwarsMarkerLocation("bed");
	}

	public boolean isEliminated() {
		return getPlayersOfState(BedwarsState.SPECTATING).size() == getTeamSize();
	}

	private Location getBedwarsMarkerLocation(String markerName) throws FileNotFoundException {
		// Get the location for the marker
		Location loc = DataHandler.read("bedwars/maps/" + this.getMap().getMapName() + "/" + this.getTeamColor() + "/" + markerName + ".json", SmallLocation.class).toLocation();
		// Update the world
		loc.setWorld(this.getMap().getWorld());
		// Return the world
		return loc;
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
}
