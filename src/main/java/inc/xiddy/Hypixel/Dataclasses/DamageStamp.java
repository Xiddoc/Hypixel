package inc.xiddy.Hypixel.Dataclasses;

public class DamageStamp {
	private HypixelPlayer damager;
	private long timestamp;

	public DamageStamp() {
	}

	public void updateDamager(HypixelPlayer damager) {
		this.damager = damager;
		this.timeStamp();
	}

	public void timeStamp() {
		this.timestamp = System.currentTimeMillis();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public HypixelPlayer getDamager() {
		return damager;
	}
}
