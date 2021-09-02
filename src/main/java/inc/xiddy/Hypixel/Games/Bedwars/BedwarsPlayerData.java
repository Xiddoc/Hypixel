package inc.xiddy.Hypixel.Games.Bedwars;

import inc.xiddy.Hypixel.Dataclasses.DamageStamp;
import inc.xiddy.Hypixel.Dataclasses.HypixelPlayer;

public class BedwarsPlayerData {
	private final HypixelPlayer player;
	private BedwarsInventory inventory;
	private BedwarsTeam team;
	private DamageStamp lastDamage;
	private long lastFireball = System.currentTimeMillis();

	public BedwarsPlayerData(HypixelPlayer player) {
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
