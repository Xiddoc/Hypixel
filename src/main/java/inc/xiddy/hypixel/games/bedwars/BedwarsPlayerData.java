package inc.xiddy.hypixel.games.bedwars;

import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.games.basegame.ingame.InGamePlayer;

public class BedwarsPlayerData {
	private final InGamePlayer player;
	private BedwarsInventory inventory;
	private BedwarsTeam team;
	private DamageStamp lastDamage;
	private long lastFireball = System.currentTimeMillis();

	public BedwarsPlayerData(InGamePlayer player) {
		// Set to field
		this.player = player;
	}

	public void setData(BedwarsTeam team) {
		this.inventory = new BedwarsInventory(team);
		this.team = team;
		this.lastDamage = new DamageStamp();
	}

	public BedwarsTeam getTeam() {
		return team;
	}

	public BedwarsInventory getBedwarsInventory() {
		return inventory;
	}

	public DamageStamp getLastDamage() {
		return lastDamage;
	}

	public HypixelPlayer getPlayer() {
		return player;
	}

	public long getLastFireball() {
		return lastFireball;
	}

	public void timestampLastFireball() {
		this.lastFireball = System.currentTimeMillis();
	}
}
