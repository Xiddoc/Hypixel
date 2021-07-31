package inc.xiddy.Hypixel.Dataclasses;

import org.bukkit.entity.Player;

public class DamageStamp {
	private Player damager;
	private long timestamp;

	public DamageStamp() { }

	public void updateDamager(Player damager) {
		this.damager = damager;
		this.timeStamp();
	}

	public void timeStamp() {
		this.timestamp = System.currentTimeMillis();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Player getDamager() {
		return damager;
	}
}
