package inc.xiddy.hypixel.games.bedwars;

import inc.xiddy.hypixel.games.basegame.ingame.InGamePlayer;

public class DamageStamp {
	private InGamePlayer damager;
	private long timestamp;

	public DamageStamp() {
	}

	public void updateDamager(InGamePlayer damager) {
		this.damager = damager;
		this.timeStamp();
	}

	public void timeStamp() {
		this.timestamp = System.currentTimeMillis();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public InGamePlayer getDamager() {
		return damager;
	}
}
